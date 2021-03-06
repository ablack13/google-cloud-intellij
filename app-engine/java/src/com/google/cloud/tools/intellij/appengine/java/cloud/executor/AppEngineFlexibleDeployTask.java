/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.intellij.appengine.java.cloud.executor;

import com.google.cloud.tools.appengine.cloudsdk.process.ProcessStartListener;
import com.google.cloud.tools.intellij.analytics.GctTracking;
import com.google.cloud.tools.intellij.analytics.UsageTrackerService;
import com.google.cloud.tools.intellij.appengine.java.AppEngineMessageBundle;
import com.google.cloud.tools.intellij.appengine.java.cloud.AppEngineDeploy;
import com.google.cloud.tools.intellij.appengine.java.cloud.AppEngineHelper;
import com.google.cloud.tools.intellij.appengine.java.cloud.flexible.AppEngineFlexibleStage;
import com.google.cloud.tools.intellij.appengine.java.sdk.CloudSdkServiceUserSettings;
import com.intellij.openapi.diagnostic.Logger;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Runnable that executes a task responsible for deploying an application to the App Engine flexible
 * environment.
 */
public class AppEngineFlexibleDeployTask extends AppEngineTask {
  private static final Logger logger = Logger.getInstance(AppEngineFlexibleDeployTask.class);

  private AppEngineDeploy deploy;
  private AppEngineFlexibleStage flexibleStage;

  public AppEngineFlexibleDeployTask(AppEngineDeploy deploy, AppEngineFlexibleStage flexibleStage) {
    this.deploy = deploy;
    this.flexibleStage = flexibleStage;
  }

  @Override
  public void execute(ProcessStartListener startListener) {
    UsageTrackerService.getInstance()
        .trackEvent(GctTracking.APP_ENGINE_DEPLOY)
        .addMetadata(GctTracking.METADATA_LABEL_KEY, "flex")
        .addMetadata(
            GctTracking.METADATA_SDK_KEY,
            CloudSdkServiceUserSettings.getInstance().getUserSelectedSdkServiceType().name())
        .ping();

    Path stagingDirectory;
    AppEngineHelper helper = deploy.getHelper();

    try {
      stagingDirectory =
          helper.createStagingDirectory(
              deploy.getLoggingHandler(),
              deploy.getDeploymentConfiguration().getCloudProjectName());
    } catch (IOException ioe) {
      deploy
          .getCallback()
          .errorOccurred(
              AppEngineMessageBundle.message(
                  "appengine.deployment.error.creating.staging.directory"));
      logger.warn(ioe);
      return;
    }

    try {
      if (!flexibleStage.stage(stagingDirectory)) {
        String message =
            AppEngineMessageBundle.message("appengine.deployment.exception.during.staging");
        deploy.getCallback().errorOccurred(message);
        logger.warn(message);
        return;
      }
    } catch (IOException e) {
      deploy
          .getCallback()
          .errorOccurred(
              AppEngineMessageBundle.message("appengine.deployment.exception.during.staging"));
      logger.error(e);
      return;
    }

    try {
      if (helper.stageCredentials(deploy.getDeploymentConfiguration().getGoogleUsername())
          == null) {
        deploy
            .getCallback()
            .errorOccurred(
                AppEngineMessageBundle.message("appengine.staging.credentials.error.message"));
        return;
      }

      deploy.deploy(stagingDirectory, startListener);
    } catch (Exception ex) {
      deploy
          .getCallback()
          .errorOccurred(
              AppEngineMessageBundle.message("appengine.deployment.exception")
                  + "\n"
                  + AppEngineMessageBundle.message("appengine.action.error.update.message"));
      logger.error(ex);
    }
  }

  @Override
  void onCancel() {
    UsageTrackerService.getInstance()
        .trackEvent(GctTracking.APP_ENGINE_DEPLOY_CANCEL)
        .addMetadata(GctTracking.METADATA_LABEL_KEY, "flex")
        .ping();
  }
}
