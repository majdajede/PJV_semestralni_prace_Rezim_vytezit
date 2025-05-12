Herní engine – Režim vytěžit

V multiplayer hře Režim vytěžit se nacházejí dvě postavičky, každá na své vlastní mapě, které se snaží vytěžit
co nejvíce zlata. Zlato mohou hráči získat tím, že rozbijí blok zlata v moment, kdy
se na něm nacházejí. Hráči si však musí dát pozor, protože každých pět sekund se jedno z políček
změní na nebezpečný blok. Pokud postavička na nebezpečný blok vstoupí, ubere se mu jeden ze
tří životů. Hráči se dostanou do další úrovně právě tehdy, když rozbijí všechny zlaté bloky a posbírají ho tím. Přestože se hráči dostanou do dalšího levelu, počet životů jim zůstává z předchozího kola.
Semestrální práce obsahuje 2 úrovně, o dvou statických mapách. Hráč 1 se může pohybovat pomocí kláves: W, A, S, D, hráč 2 pomocí šipek.
Kámen lze rozbít klávesou M (hráč 1) nebo B (hráč 2), pokud postavička „stojí“ na kameni
(jednom políčku). Hra je z ptačí perspektivy a ukládání stavu je řešeno serializací.

Cíl hry: posbírat všechno zlato

Entity:

Postavička:

➢ Hráč 1 se pohybuje pomocí W, A, S, D

➢ Hráč 2 se pohybuje pomocí šipek

➢ Hráč 1 rozbíjí bloky pomocí B, Hráč 2 klávesou M, musí stát na kameni

➢ Má 3 životů (každý zvlášť)

Nebezpečný blok:

➢ Mění se po časovém intervalu

➢ Náhodně generovaný po časovém intervalu

➢ Pokud se ocitne postavička na tomto bloku, tak se jí ubere život

Zlatý blok:

➢ Hráč ho musí rozbít pomocí M nebo B

➢ Když ho hráč rozbije, tak získá zlato

➢ Pokud je posbíráno všechno zlato, hráč postupuje do dalšího levelu

Blok (hrací pole):

➢ více bloků generují mapu hry (hrací pole)

Rozšíření ve dvojici:
semestrální práce bude obsahovat dvě vlákna (1 vlákno jeden hráč) => na
displeji se vykreslí dvě samostatná hrací pole. Hráči se přesunou na další úroveň až poté, co jsou
rozbité všechny kameny a posbírány všechny diamanty. Dále v rozšíření pro dvojici bude
převádění objektu na string (serializace)

Barbora Gregorová a Magdalena Lebedová