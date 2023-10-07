package ui.project.dialogs

import app.Salient
import org.yunghegel.gdx.ui.widgets.SLabel
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.STextButton

class ProjectDialog :BaseInspectorDialog(DialogType.PROJECT) {

    override fun build() {
        buildScaffold()
        var label = SLabel("Project")
        add(label).expandX().left().height(20f).growX().row()
    }



}