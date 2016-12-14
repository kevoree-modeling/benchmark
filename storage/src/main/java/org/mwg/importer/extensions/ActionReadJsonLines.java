package org.mwg.importer.extensions;

import org.mwg.task.Action;
import org.mwg.task.TaskContext;

/**
 * Created by ludovicmouline on 09/12/2016.
 */
public class ActionReadJsonLines implements Action{
    private final String _lines;


    ActionReadJsonLines(String _lines) {
        this._lines = _lines;
    }

    @Override
    public void eval(TaskContext ctx) {
//        TaskResult previous = ctx.result();
//        TaskResult next = ctx.newResult();
//
//        for(int i=0;i<previous.size();i++) {
//            if(previous.get(i) instanceof String) {
//                next = JsonValueResultBuilder.build(Json.parse((String) previous.get(i)));
//                break;
//            }
//        }
//
//        previous.clear();
//        ctx.continueWith(next);
    }

    @Override
    public void serialize(StringBuilder builder) {

    }
}
