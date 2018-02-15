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

package com.google.cloud.tools.intellij.startup;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.cloud.tools.intellij.login.IntegratedGoogleLoginService;
import com.google.cloud.tools.intellij.service.PluginConfigurationService;
import com.google.cloud.tools.intellij.service.PluginInfoService;
import com.google.cloud.tools.intellij.testing.BasePluginTestCase;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/** Tests to validate initialization on supported platforms */
@RunWith(MockitoJUnitRunner.class)
public class AccountPluginInitializationComponentTest extends BasePluginTestCase {

  private static final String PLUGIN_ID_STRING = "com.google.gct.core";
  @Mock PluginInfoService pluginInfoService;
  @Mock PluginConfigurationService pluginConfigurationService;
  @Mock IntegratedGoogleLoginService googleLoginService;

  AccountPluginInitializationComponent testComponent;

  @Before
  public void registerMockServices() {
    registerService(PluginInfoService.class, pluginInfoService);
    registerService(PluginConfigurationService.class, pluginConfigurationService);
    registerService(IntegratedGoogleLoginService.class, googleLoginService);
    testComponent = new AccountPluginInitializationComponent();
  }

  @Test
  public void testInitComponent_usageTrackingIsEnabled() {
    Application mockApplication = spy(ApplicationManager.getApplication());
    when(mockApplication.isUnitTestMode()).thenReturn(false);
    ApplicationManager.setApplication(mockApplication, mock(Disposable.class));
    AccountPluginInitializationComponent testComponent =
        spy(new AccountPluginInitializationComponent());
    doNothing().when(testComponent).configureUsageTracking();
    testComponent.initComponent();
    verify(testComponent).configureUsageTracking();
  }

  @Test
  public void testInitComponent_usageTrackingIsDisabled() {
    Application mockApplication = spy(ApplicationManager.getApplication());
    when(mockApplication.isUnitTestMode()).thenReturn(true);
    ApplicationManager.setApplication(mockApplication, mock(Disposable.class));
    AccountPluginInitializationComponent testComponent =
        spy(new AccountPluginInitializationComponent());
    testComponent.initComponent();
    verify(testComponent, never()).configureUsageTracking();
  }

  @Test
  public void testInitComponent_loginServiceIsInitialized() {
    testComponent.initComponent();
    verify(googleLoginService).loadPersistedCredentials();
  }
}
