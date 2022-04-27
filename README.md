<div style="text-align:center"><img src="https://playprojectcreate.com/ProjectCreate-Logo.png"  alt="Logo"/></div>

[![Version](https://img.shields.io/badge/version-0.0.0-blue.svg?style=for-the-badge&logo=version)]()
[![Build Status](https://img.shields.io/badge/build-success-brightgreen?style=for-the-badge&logo=jenkins)](http://jenkins.playprojectcreate.com/job/project-create/)

> 2D Sandbox Game heavily focused on freedom of gameplay and exploration.

## Table of Contents
1. [Technologies](#technologies)
2. [Installation](#Installation)
3. [Usage](#usage)
4. [Plugin Installation](#plugin-installation-kamino)
5. [Author](#author)
6. [License](#license)

> SE_06 All relevant code is in the kamino & kamino-api directories, most of the database code is in kamino/src/de/undefinedhuman/projectcreate/kamino/database/Couchbase.java

## Technologies
Project is created with:
* Java JDK Version 17
* Gradle 7.4 (wrapper files already included in the repository)
* Additional java dependencies can be found in the root [build.gradle](https://gitlab.playprojectcreate.com/undefinedhuman/project-create/-/blob/main/build.gradle) file

## Prerequisites

Create a personal access token (GitLab, scope: api) to allow gradle to download dependencies from the project's private package repository.
[Gitlab Docs](https://docs.gitlab.com/ee/user/packages/maven_repository/#authenticate-with-a-personal-access-token-in-gradle)

> IMPORTANT! Insert the token into your local global gradle directory located in the home folder and never commit it to the repository!

## Installation
Required dependencies are automatically downloaded by Gradle. 

```sh
# Clone repository
git clone https://gitlab.playprojectcreate.com/undefinedhuman/project-create.git
cd project-create/
```

### Usage
```sh
# Run either part of the project except the server by replacing "SUB_PROJECT_NAME" with either "game", "editor", "updater" or "launcher" to launch the specific sub project
./gradlew :SUB_PROJECT_NAME:run
```

### Server Usage
It is recommended for running a local development server to use arguments that place the server directory in the home folder instead of in the repository.

```sh
# The server will create the folder containing configs/logs/etc. with path ~/.projectcreate/server and set the log level to debug
./gradlew :SUB_PROJECT_NAME:run --args='".projectcreate/server" "External" "debug"'
```

### Plugin Installation (Kamino)
Plugin installation can be done by placing the plugin jar file in the plugin folder of the server.

Example: 
Server directory: ~/.projectcreate/server \
Plugin: [Kamino](https://gitlab.playprojectcreate.com/undefinedhuman/project-create/-/package_files/309/download) (kamino.jar)

Place the jar file (kamino.jar) in ~/.projectcreate/server/plugins/kamino.jar, and run the server as usual.

## Contribute

> Please check in with the author before committing anything to the repo

## Author 

Alexander Padberg <undefinedhuman>

## License

Copyright © 2014-2022 [Alexander Padberg](https://playprojectcreate.com)
