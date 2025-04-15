Herní engine – Režim vytěžit

Ve hře Těžíme se nacházejí dvě postavičky, každá na svém hracím poli 6x6, které se snaží vytěžit
co nejvíce surovin. Suroviny (diamanty) musí postavička získat tím, že rozbije kámen, za kterým
se skrývají. Pracovník dolu si však musí dát pozor, protože po pár sekundách se jedno z políček
změní na nebezpečný blok. Pokud postavička na nebezpečný blok vstoupí, ubere se mu jeden z
pěti životů. Hráči se dostanou do další úrovně právě tehdy když rozbijí všechny kameny (posbírají
diamanty). Jakmile se hráči dostane do dalšího levelu, počet životů se opět vrátí na maximum (5
životů).
Semestrální práce bude obsahovat 2 úrovně a mapa nebude dynamicky generovaná. Hráč 1 se
bude moci pohybovat pomocí kláves: W, A, S, D, hráč 2 pomocí šipek.
Kámen lze rozbít klávesou M (hráč 2) nebo B (hráč 1), pokud postavička „stojí“ na kameni
(jednom políčku).
Hra bude z ptačí perspektivy.
Ukládání stavu bude řešeno serializací.

Cíl hry: posbírat všechny diamanty

Entity:

Postavička:

➢ Pohybuje se W, A, S, D, hráč 2 šipkami

➢ Hráč 1 rozbíjí bloky pomocí B hráč 2 klávesou M, musí stát na kameni

➢ Má 5 životů (každý zvlášť)

Nebezpečný blok:

➢ Mění se po časovém intervalu

➢ Náhodně generovaný po časovém intervalu

➢ Pokud se ocitne postavička na tomto bloku, tak se jí ubere život

Kámen:

➢ Hráč ho musí rozbít pomocí M nebo B

➢ Když ho hráč rozbije, tak získá diamanty

➢ Pokud jsou rozbité všechny kameny, hráč postupuje do dalšího levelu

Blok (hrací pole):

➢ více bloků generují mapu hry (hrací pole)

Rozšíření ve dvojici:
semestrální práce bude obsahovat dvě vlákna (1 vlákno jeden hráč) => na
displeji se vykreslí dvě samostatná hrací pole. Hráči se přesunou na další úroveň až poté, co jsou
rozbité všechny kameny a posbírány všechny diamanty. Dále v rozšíření pro dvojici bude
převádění objektu na string (serializace)

Barbora Gregorová a Magdalena Lebedová