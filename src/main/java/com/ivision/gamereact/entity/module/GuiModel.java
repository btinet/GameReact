package com.ivision.gamereact.entity.module;

import javafx.scene.Group;

import java.util.HashMap;
import java.util.Map;

public class GuiModel {

    private final HashMap<Integer, Group> modules = new HashMap<>();

    public Group getModule (int symbolId) {

        registerModules();
        if(this.modules.containsKey(symbolId)) {
            return this.modules.get(symbolId);
        } else {
            System.out.printf("Symbol %s ist keinem Modul zugeordnet! %n",symbolId);
            return new Group();
        }

    }

    private void registerModules () {
        this.modules.putAll(Map.of(
                1, new TextModule(1),
                2, new TextModule(2)
        ));
    }

}
