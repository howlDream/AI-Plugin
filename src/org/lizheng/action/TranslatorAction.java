package org.lizheng.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.ui.popup.ActiveIcon;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.panels.VerticalBox;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.lizheng.TranslationYoudao;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;


/**
 * 翻译文本
 * @author zheng.li
 */
public class TranslatorAction extends AnAction {

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
        assert position != null;
        String ret = TranslationYoudao.doTranslate(selectedText);


        VerticalBox vbox = new VerticalBox();
        // 添加空白区域，使窗口内容与边缘保持一定距离
        vbox.add(Box.createVerticalStrut(5));

        // 创建文本框和滚动面板，并添加到内容面板中
        JBTextArea textArea = new JBTextArea(ret);
        // 设置文字颜色为白色
        textArea.setForeground(JBColor.WHITE);
        // 设置背景色为深灰色
        textArea.setBackground(JBColor.GRAY);
        // 设置字体和字号
        textArea.setFont(UIManager.getFont("TextArea.font").deriveFont(15f));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JBScrollPane(textArea);
        vbox.add(scrollPane);
        // 添加空白区域
        vbox.add(Box.createVerticalStrut(10));




        // 创建一个白色边框，并设置其外观和大小
        Border border = BorderFactory.createLineBorder(JBColor.DARK_GRAY, 1);
        scrollPane.setBorder(border);
        Dimension size = new Dimension(500, 300);
        // 加载指定路径下的图标文件
        Icon titleIcon = IconLoader.getIcon("/images/translate.png");
        ActiveIcon activeIcon = new ActiveIcon(titleIcon, titleIcon);
        // 创建一个自定义的弹出窗口，并设置其属性
        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(vbox, textArea)
                .setCancelOnClickOutside(true)
                .setCancelOnOtherWindowOpen(false)
                .setCancelKeyEnabled(true)
                .setMovable(true)
                .setTitleIcon(activeIcon)
                .setResizable(false)
                .setRequestFocus(true)
                .setTitle("翻译结果")
                .setAdText("有道翻译接口提供")
                .setShowShadow(true)
                .setShowBorder(true)
                // 设置边框颜色
                .setBorderColor(new JBColor(new Color(0xFFFFFF),new Color(0x888888)))
                .setMinSize(size)
                .setDimensionServiceKey(null, "Popup", false)
                .createPopup();

        // 设置窗口位置和大小
        popup.setSize(size);

        // 显示弹出窗口
        popup.showCenteredInCurrentWindow(Objects.requireNonNull(event.getProject()));



    }

}
