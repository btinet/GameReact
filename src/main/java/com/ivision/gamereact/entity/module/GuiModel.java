package com.ivision.gamereact.entity.module;

import javafx.scene.Group;

import java.util.HashMap;
import java.util.Map;

public class GuiModel {

    private HashMap<Integer, Group> modules;

    public GuiModel () {
        registerModules();
    }

    public Group getModule (int symbolId) {


        if(this.modules.containsKey(symbolId)) {
            return this.modules.get(symbolId);
        } else {
            System.out.printf("Symbol %s ist keinem Modul zugeordnet! %n",symbolId);
            return new Group();
        }

    }

    /**
     * Konfiguration der Marker abrufen.
     */
    private void registerModules () {
        this.modules = new HashMap<>(Map.of(
                1, new TextModule(
                        1,
                        "Sagt es niemand, nur den Weisen,\n" +
                                "Weil die Menge gleich verhöhnet:\n" +
                                "Das Lebendge will ich preisen,\n" +
                                "Das nach Flammentod sich sehnet."
                ),
                2, new TextModule(
                        2,
                        "In der Liebesnächte Kühlung,\n" +
                                "Die dich zeugte, wo du zeugtest,\n" +
                                "Überfällt dich fremde Fühlung,\n" +
                                "Wenn die stille Kerze leuchtet."
                ),
                3, new TextModule(
                        3,
                        "Nicht mehr bleibest du umfangen\n" +
                                "In der Finsternis Beschattung,\n" +
                                "Und dich reißet neu Verlangen\n" +
                                "Auf zu höherer Begattung."
                ),
                4, new TextModule(
                        4,
                        "Und solang du das nicht hast,\n" +
                                "Dieses: Stirb und werde!\n" +
                                "Bist du nur ein trüber Gast\n" +
                                "Auf der dunklen Erde."
                ),
                5,new TextModule(
                        5, "Goethe: Selige Sehnsucht"
                )
        ));

        // TODO: Daten von Datenbank abrufen, lokal speichern und für Ausgabe vorbereiten.

    }

}
