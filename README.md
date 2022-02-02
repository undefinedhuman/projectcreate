<div style="text-align:center"><img src="http://playprojectcreate.com/ProjectCreate-Logo.png"  alt="Logo"/></div>

[![Version](https://img.shields.io/badge/version-0.0.0-blue.svg?style=for-the-badge&logo=version)]()
[![Build Status](https://img.shields.io/badge/build-success-brightgreen?style=for-the-badge&logo=jenkins)](http://jenkins.playprojectcreate.com/job/project-create/)

> 2D Sandbox Game heavily focused on freedom of gameplay and exploration.

## Table of Contents
1. [Technologies](#technologies)
2. [Installation](#Installation)
3. [Usage](#usage)
4. [Documentation](#documentation)
5. [License](#license)

## Technologies
Project is created with:
* Java JDK Version 8
* Gradle 6.8.3 (wrapper files already included in the repository)
* Additional dependencies can be found in the root [build.gradle](http://gitlab.playprojectcreate.com/undefinedhuman/project-create/-/blob/main/build.gradle) file

## Installation
> Required dependencies are automatically downloaded by Gradle.
> Personally, I suggest using Intellij IDE for easier installation and handling of the project.

```sh
# Clone repository
git clone https://gitlab.playprojectcreate.com/undefinedhuman/project-create.git
cd project-create/
```

## Usage
```sh
# Run either part of the project by replacing "SUB_PROJECT_NAME" with either "game, editor, server, updater or launcher" to launch the specific sub project
./gradlew :SUB_PROJECT_NAME:run
```

## License

Copyright © 2014-2022 [Alexander Padberg](https://playprojectcreate.com)
