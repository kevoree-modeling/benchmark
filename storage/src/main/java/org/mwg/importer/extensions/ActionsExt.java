package org.mwg.importer.extensions;

import org.mwg.task.Action;

public class ActionsExt {
    public static Action readJsonLines(String lines) {
        return new ActionReadJsonLines(lines);
    }
}
