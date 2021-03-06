![Google Cloud Platform Logo](https://cloud.google.com/_static/images/gcp-logo.png)
# Google Cloud Tools for IntelliJ plugin

|  | Build Status | 
| :--- | :---: |
| Ubuntu | ![Build Status](https://storage.googleapis.com/cloud-tools-for-java-kokoro-build-badges/intellij-ubuntu-master-orb.png) |
| Windows | ![Build Status](https://storage.googleapis.com/cloud-tools-for-java-kokoro-build-badges/intellij-windows-master-orb.png) |
| MacOS | ![Build Status](https://storage.googleapis.com/cloud-tools-for-java-kokoro-build-badges/intellij-macos-master-orb.png) |
| Previous Major Version| ![Build Status](https://storage.googleapis.com/cloud-tools-for-java-kokoro-build-badges/intellij-ubuntu-master-previous-version-orb.png) |
| Latest EAP Snapshot| ![Build Status](https://storage.googleapis.com/cloud-tools-for-java-kokoro-build-badges/intellij-ubuntu-master-eap-orb.png) |

The plugin integrates the [Google Cloud Platform](https://cloud.google.com/)
into the IntelliJ IDEA UI. Currently this includes:

* [Google Cloud Source Repositories](https://cloud.google.com/tools/cloud-repositories/) 
  Fully-featured private Git repositories hosted on Google Cloud Platform.
* The [Google Cloud Debugger](https://cloud.google.com/tools/cloud-debugger/) 
  The Cloud Debugger can inspect the state of a Java application running on 
  [Google App Engine](https://cloud.google.com/appengine/)
  at any code location without stopping the application.
* [Google App Engine](https://cloud.google.com/appengine/docs/) deployment via the Cloud SDK.
* [Google Cloud Storage](https://cloud.google.com/storage/) 
  Browse your Google Cloud Storage buckets.
* [Google Cloud Java Client Libraries](https://cloud.google.com/tools/intellij/docs/client-libraries) 
  Add Java client libraries to your project, enable Google Cloud APIs, and create service accounts.

For detailed user documentation go to our documentation
 [website](https://cloud.google.com/tools/intellij/docs/?utm_source=github&utm_medium=google-cloud-intellij&utm_campaign=ToolsforIntelliJ).

## Supported Platforms

The Cloud Tools for IntelliJ plugin primarily supports the following IDEs, version 2017.1 or later:

* IntelliJ IDEA Community Edition 
* IntelliJ IDEA Ultimate Edition

In addition to IntelliJ IDEA, the plugin provides limited support for the following IDEs, version 2017.1 or later:
* PyCharm (Professional and Community Editions)
* WebStorm
* PhpStorm
* Rider
* CLion
* RubyMine
* GoLand
* AppCode

This table displays the features available in the IDEs:

| | IntelliJ IDEA - Ultimate | IntelliJ IDEA - Community | All other IntelliJ platform based IDEs |
|---|:---:|:---:|:---:|
| Create App Engine standard environment App | :heavy_check_mark: | :x: | :x: |
| Create App Engine flexible environment App | :heavy_check_mark: | :heavy_check_mark: | :x: |
| Run and Debug App Engine<br>standard environment App Locally | :heavy_check_mark: | :heavy_check_mark: \* | :x: |
| Deploy App Engine flexible environment App | :heavy_check_mark: | :heavy_check_mark: | :x: |
| Deploy App Engine standard environment App | :heavy_check_mark: | :heavy_check_mark: \** | :x: |
| Manage Cloud Client Libraries | :heavy_check_mark: | :heavy_check_mark: | :x: |
| Browse files in Cloud Storage | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Use VCS for Source Control | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: |
| Debug using Stackdriver | :heavy_check_mark: | :heavy_check_mark: | :x: |
| Auto-manage the Cloud SDK | :heavy_check_mark: | :heavy_check_mark: | :x: |

<p><b>*</b> You can follow the
  <a href="https://cloud.google.com/tools/intellij/docs/deploy-local#debugging_your_application_locally_on_community_edition">debugging your
    application locally on Community Edition</a> instructions to use the Maven
  or Gradle plugins for your local run.</p>

<p><b>**</b> You can deploy Maven-based projects using the IntelliJ IDEA
  Community Edition to the App Engine standard environment.</p>
  
## Installation

You can find our plugin in the Jetbrains plugin repository by going to IntelliJ -> Settings -> Browse Repositories, and search for 'Google Cloud Tools'. 

### Pre-releases 

The pre-release binaries are being deployed to the Jetbrains plugin repository on an alpha
channel. To install them please perform the following steps:

1. Install the Google Cloud Tools plugin
    1. Copy this URL `https://plugins.jetbrains.com/plugins/alpha/8079`
    1. Use the copied URL as the Custom Plugin URL when following [these instrucions](https://www.jetbrains.com/idea/help/managing-enterprise-plugin-repositories.html)
    1. Search for the 'Google Cloud Tools' plugin and install it.

You can also grab the latest nightly build of the plugin by following the same steps as above but 
replacing 'alpha' with 'nightly' in the URLs.

If you wish to build this plugin from source, please see the
[contributor instructions](https://github.com/GoogleCloudPlatform/google-cloud-intellij/blob/master/CONTRIBUTING.md).

## FAQ


**None yet**
