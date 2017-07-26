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

package com.google.cloud.tools.intellij.appengine.cloud;

import static com.google.cloud.tools.intellij.appengine.sdk.CloudSdkValidationResult.CLOUD_SDK_NOT_FOUND;
import static com.google.cloud.tools.intellij.appengine.sdk.CloudSdkValidationResult.CLOUD_SDK_VERSION_NOT_SUPPORTED;
import static com.google.cloud.tools.intellij.appengine.sdk.CloudSdkValidationResult.NO_APP_ENGINE_COMPONENT;
import static com.google.cloud.tools.intellij.testing.TestUtils.expectThrows;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.intellij.appengine.cloud.flexible.UserSpecifiedPathDeploymentSource;
import com.google.cloud.tools.intellij.appengine.project.AppEngineProjectService;
import com.google.cloud.tools.intellij.appengine.project.AppEngineProjectService.FlexibleRuntime;
import com.google.cloud.tools.intellij.appengine.project.MalformedYamlFileException;
import com.google.cloud.tools.intellij.appengine.sdk.CloudSdkService;
import com.google.cloud.tools.intellij.testing.CloudToolsRule;
import com.google.cloud.tools.intellij.testing.TestService;
import com.google.common.collect.ImmutableSet;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.remoteServer.configuration.RemoteServer;
import com.intellij.remoteServer.configuration.deployment.DeploymentSource;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

/** Unit tests for {@link AppEngineDeploymentConfiguration}. */
@RunWith(JUnit4.class)
public final class AppEngineDeploymentConfigurationTest {

  @Rule public final CloudToolsRule cloudToolsRule = new CloudToolsRule(this);

  @Mock private RemoteServer<AppEngineServerConfiguration> mockRemoteServer;
  @Mock private AppEngineDeployable mockAppEngineDeployable;
  @Mock private UserSpecifiedPathDeploymentSource mockUserSpecifiedPathDeploymentSource;
  @Mock private DeploymentSource mockOtherDeploymentSource;
  @Mock @TestService private CloudSdkService mockCloudSdkService;
  @Mock @TestService private AppEngineProjectService mockAppEngineProjectService;

  private AppEngineDeploymentConfiguration configuration;

  @Before
  public void setUp() throws Exception {
    when(mockAppEngineDeployable.isValid()).thenReturn(true);
    configuration = new AppEngineDeploymentConfiguration();
  }

  @Test
  public void checkConfiguration_withValidFlexConfig_doesNotThrow() throws Exception {
    setUpValidFlexConfiguration();
    configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable);
  }

  @Test
  public void checkConfiguration_withValidCustomFlexConfig_doesNotThrow() throws Exception {
    setUpValidCustomFlexConfiguration();
    configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable);
  }

  @Test
  public void checkConfiguration_withValidStandardConfig_doesNotThrow() throws Exception {
    setUpValidStandardConfiguration();
    configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable);
  }

  @Test
  public void checkConfiguration_withOtherDeploymentSource_throwsException() {
    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockOtherDeploymentSource));
    assertThat(error).hasMessage("Invalid deployment source.");
  }

  @Test
  public void checkConfiguration_withCloudSdkNotFound_throwsException() {
    setUpValidFlexConfiguration();
    when(mockCloudSdkService.validateCloudSdk()).thenReturn(ImmutableSet.of(CLOUD_SDK_NOT_FOUND));

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error).hasMessage("No Cloud SDK was found in the specified directory.");
  }

  @Test
  public void checkConfiguration_withOutdatedCloudSdkVersion_throwsException() {
    setUpValidFlexConfiguration();
    when(mockCloudSdkService.validateCloudSdk())
        .thenReturn(ImmutableSet.of(CLOUD_SDK_VERSION_NOT_SUPPORTED));

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error.getMessage()).contains("The Cloud SDK is out of date.");
  }

  @Test
  public void checkConfiguration_withNoAppEngineComponent_throwsException() {
    setUpValidFlexConfiguration();
    when(mockCloudSdkService.validateCloudSdk())
        .thenReturn(ImmutableSet.of(NO_APP_ENGINE_COMPONENT));

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error.getMessage())
        .contains("The Cloud SDK does not contain the app-engine-java component.");
  }

  @Test
  public void checkConfiguration_withInvalidDeployable_throwsException() {
    setUpValidFlexConfiguration();
    when(mockAppEngineDeployable.isValid()).thenReturn(false);

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error).hasMessage("Select a valid deployment source.");
  }

  @Test
  public void checkConfiguration_withBlankCloudProject_throwsException() {
    setUpValidFlexConfiguration();
    configuration.setCloudProjectName("");

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error).hasMessage("Please select a project.");
  }

  @Test
  public void checkConfiguration_withUserSpecifiedSource_andNoArtifactPath_throwsException() {
    setUpValidFlexConfiguration();
    when(mockUserSpecifiedPathDeploymentSource.isValid()).thenReturn(true);
    when(mockUserSpecifiedPathDeploymentSource.getEnvironment())
        .thenReturn(AppEngineEnvironment.APP_ENGINE_FLEX);
    configuration.setUserSpecifiedArtifactPath("");

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () ->
                configuration.checkConfiguration(
                    mockRemoteServer, mockUserSpecifiedPathDeploymentSource));
    assertThat(error).hasMessage("Browse to a JAR or WAR file.");
  }

  @Test
  public void checkConfiguration_withUserSpecifiedSource_andNotAJarOrWar_throwsException() {
    setUpValidFlexConfiguration();
    when(mockUserSpecifiedPathDeploymentSource.isValid()).thenReturn(true);
    when(mockUserSpecifiedPathDeploymentSource.getEnvironment())
        .thenReturn(AppEngineEnvironment.APP_ENGINE_FLEX);
    configuration.setUserSpecifiedArtifactPath("some-file.txt");

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () ->
                configuration.checkConfiguration(
                    mockRemoteServer, mockUserSpecifiedPathDeploymentSource));
    assertThat(error).hasMessage("Browse to a JAR or WAR file.");
  }

  @Test
  public void checkConfiguration_withBlankModuleName_throwsException() throws Exception {
    setUpValidCustomFlexConfiguration();
    configuration.setModuleName("");

    RuntimeConfigurationError error =
        expectThrows(
            RuntimeConfigurationError.class,
            () -> configuration.checkConfiguration(mockRemoteServer, mockAppEngineDeployable));
    assertThat(error).hasMessage("Browse to an app.yaml file.");
  }

  /** Sets up the {@code configuration} to be valid for a deployment to a flex environment. */
  private void setUpValidFlexConfiguration() {
    when(mockCloudSdkService.validateCloudSdk()).thenReturn(ImmutableSet.of());
    when(mockAppEngineDeployable.isValid()).thenReturn(true);
    when(mockAppEngineDeployable.getEnvironment()).thenReturn(AppEngineEnvironment.APP_ENGINE_FLEX);
    configuration.setCloudProjectName("some-project-name");
    configuration.setUserSpecifiedArtifactPath("something.war");
    configuration.setModuleName("some-module-name");
  }

  /** Sets up the {@code configuration} to be a valid custom deployment to a flex environment. */
  private void setUpValidCustomFlexConfiguration() {
    configuration = new AppEngineDeploymentConfiguration();

    when(mockCloudSdkService.validateCloudSdk()).thenReturn(ImmutableSet.of());
    when(mockAppEngineDeployable.isValid()).thenReturn(true);
    when(mockAppEngineDeployable.getEnvironment()).thenReturn(AppEngineEnvironment.APP_ENGINE_FLEX);

    String appYamlPath = "some-app.yaml";
    configuration.setCloudProjectName("some-project-name");
    configuration.setUserSpecifiedArtifactPath("something.war");
    configuration.setModuleName("some-module-name");

    try {
      when(mockAppEngineProjectService.getFlexibleRuntimeFromAppYaml(appYamlPath))
          .thenReturn(Optional.of(FlexibleRuntime.CUSTOM));
    } catch (MalformedYamlFileException e) {
      throw new AssertionError("This should not happen; Mockito must be broken.", e);
    }
  }

  /** Sets up the {@code configuration} to be valid for a deployment to a standard environment. */
  private void setUpValidStandardConfiguration() {
    when(mockCloudSdkService.validateCloudSdk()).thenReturn(ImmutableSet.of());
    when(mockAppEngineDeployable.isValid()).thenReturn(true);
    when(mockAppEngineDeployable.getEnvironment())
        .thenReturn(AppEngineEnvironment.APP_ENGINE_STANDARD);
    configuration.setCloudProjectName("some-project-name");
  }
}