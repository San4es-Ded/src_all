package ru.prism.screen.editor;

import ru.prism.screen.CrosshairEditor;
import ru.prism.screen.HandsEditor;
import ru.prism.screen.PreviewEditor;

/** Реестр оверлей-редакторов: одновременно активным может быть только один. */
public final class OverlayEditors {

    /** Проверяется каждый кадр и на каждый клик, поэтому список собран один раз. */
    private static final OverlayEditor[] ALL = {
            HandsEditor.getInstance(),
            PreviewEditor.getInstance(),
            CrosshairEditor.getInstance()
    };

    private OverlayEditors() {
    }

    public static OverlayEditor[] all() {
        return ALL;
    }

    public static OverlayEditor active() {
        for (OverlayEditor editor : ALL) {
            if (editor.isActive()) return editor;
        }
        return null;
    }

    public static boolean anyActive() {
        return active() != null;
    }

    /** Меню закрывается — гасим всё, что могло остаться открытым. */
    public static void closeAll() {
        for (OverlayEditor editor : all()) {
            if (editor.isActive()) editor.closeFromMenuRemoval();
        }
    }
}
