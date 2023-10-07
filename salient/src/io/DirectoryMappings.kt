package io

object DirectoryMappings {

    val USER_HOME = System.getProperty("user.home")

    val PROJECTS_DIR = "$USER_HOME/.salient/projects"

    val CONFIG_FILEPATH = "$USER_HOME/.salient/salient.config"

    val TMP_FILEPATH = "$USER_HOME/.salient/salient.tmp"

    val LOG_FILEPATH = "$USER_HOME/.salient/salient.log"

    val SCENE_EXTENSION = ".scene"

    val PROJECT_EXTENSION = ".salient"

    fun getProjectPath(projectName: String) = "$PROJECTS_DIR/$projectName/$projectName.salient"

    fun getAssetsPath(projectName: String) = "$PROJECTS_DIR/$projectName/assets"

    fun getScenesPath(projectName: String) = "$PROJECTS_DIR/$projectName/scenes"

    fun getScenePath(projectName: String, sceneName: String) = "$PROJECTS_DIR/$projectName/scenes/$sceneName.scene"


}