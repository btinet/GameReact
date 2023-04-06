# Beispielanwendungen

1. [Pong React](#pong-react)

## Pong React

**Pong React** ist die ReacTIVision-Implementation des Spieleklassikers "Pong". Hierbei
werden die Schläger allerdings nicht mit der Tastatur, sondern mit Marker-Würfeln gespielt.
Je nach Rotationswinkel wird der Schläger entweder nach links oder nach rechts bewegt.

Ziel des Spiels ist es, den Tennisball ins gegnerische Aus zu manövrieren.

![draft_pong_react_230327-01.png](./assets/images/sketch/draft_pong_react_230327-01.png)

Die Skizze zeigt, wie der Rotationswinkel in Richtung und Geschwindigkeit übersetzt wird.

Das Spielfeld sieht im ersten Entwurf folgendermaßen aus:

![draft_pong_react_hui_230327-01.png](./assets/images/sketch/draft_pong_react_hui_230327-01.png)

Da ein Beamer oder ein Monitor normalerweise im Querformat arbeitet, wurde das Layout um 90° gedreht.
Einige optische Änderungen wurden ebenfalls vorgenommen. So sieht das aktuelle Layout aus:

![img.png](img.png)

Im Vollbildmodus wird die Spielfläche nicht skaliert, wodurch die Kreisausschnitte an den
Bildschirmrändern mehr Platz zum Ablegen der Marker bieten:

![img_1.png](img_1.png)

Über einen TUIO-Simulator können wir die Spielfiguren testweise auch
ohne vorhandene Kamera steuern. Wird kein entsprechendes Marker-Singal
gesendet, bewegen sich die Paddles selbständig.

![img_2.png](img_2.png)

Über Fingerberührung auf dem Spielfeld kann zwischen Pause- und Spielmodus
gewechselt werden. Alternativ geschieht das aber auch über die ``F1``-Taste.

