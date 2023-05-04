package org.lizheng.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 连接AI
 * @author zheng.li
 */
public class ChartGptAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //获取用户所在的编辑器对象（就是界面）
        Editor editor= event.getData(PlatformDataKeys.EDITOR);
        //通过编辑器获取选中对象
        assert editor != null;
        SelectionModel model = editor.getSelectionModel();
        //获取模型中的文本
        String selectedText = model.getSelectedText();
        VisualPosition position = model.getSelectionStartPosition();
        if(StringUtils.isEmpty(selectedText)){
            return;
        }

    }
}
