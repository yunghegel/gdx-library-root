package ui.project.dialogs

import com.badlogic.gdx.files.FileHandle
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.STextButton
import javax.swing.JPopupMenu.Separator

abstract class BaseInspectorDialog (var type: DialogType) : STable() {

    init {
        buildScaffold()
    }

    abstract fun build();

    fun makeTitleTable(): STable {
        var title = type.name
        var label = STextButton(title)
        var table = STable()
        table.add(label).expandX().left().height(20f).growX()
        return table
    }

    fun buildScaffold() {
        var titleTable = makeTitleTable()
        add(titleTable).expandX().left().height(20f).growX().row()
    }


    fun match(newType: DialogType): Boolean {

        return this.type == newType;
    }

    init {
        build();
    }

}
