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

package com.google.cloud.tools.intellij.appengine.facet.standard;

import com.google.cloud.tools.intellij.ui.GoogleCloudToolsIcons;
import com.google.cloud.tools.intellij.util.GctBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Creates a shortcut in the tools menu to add the App Engine Standard facet to a module.
 */
public class AddAppEngineStandardFacetToolsMenuAction extends AnAction
{
  public AddAppEngineStandardFacetToolsMenuAction() {
    super(GctBundle.message("appengine.standard.facet.name"),
        GctBundle.message("appengine.add.standard.facet.tools.menu.description"),
        GoogleCloudToolsIcons.APP_ENGINE);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {

  }
}