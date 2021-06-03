# ALG2 SEM

Aplikace pro šifrování souborů do obrázků formátu ppm.

## Zadání
Cílem mé práce bylo vytvořit program, který by umožnoval skrýt libolná data do obrázku, tak aby se dopad zápisu projevil pouze jako přidání lidskému oku jinak nespatřitelného šumu (v závislti na "hloubce" zápisu dat).
Práce by dále měla umožnovat data zpětně z obrázku přečíst a uložit na disk. Uložit do obrázku více různých souborů. Třídit načtená data podle zvolených paramterů.

## Návrh řešení
Jako výchozí formát obrázku jsem zvolil PPM, s magickou hodnotou P3. 
PPM je formát, který je sice velice nešetrný co se týče velikosti na disku, ale jeho čtení je opravdu jednoduché. 
Typ P3 kóduje informaci barvě (true color), 3 kanály (R,G,B) v podobě čísel 0-255 zapsaných jako znaky ASCII do textového souboru. 
Je tak jednoduché kontrolovat výsledek manipulace dat.
Aby zapsaná data nebyla člověkem spatřitelná, využívá se zápisu ze strany LSB (least significatn bit), například pokud je jeden barevný kanál kódovaný do 8-bitů, pak MSB (1. bit z leva) nabívá hodnot 0/128, zatím LSB (1. bit z prava) 0/1. 
Na rozsahu 256 hodnot se při hloubce zápisu 1-bit doupouštíme zkreslení -+1 (cca <0,5%), což běžným okem na obrázcích typu fotografie krajiny je nespatřitelné. 
Můj program umožnuje zápis 1/8 (1-bit dat na 8-bitů obrazu) až 8/8, kde však již dochází ke 100% zkreslení původního obrazu.

### Funkční specifikace
1. Vybrat obrázek pro uložení dat
1. Vybrat hloubku zápisu dat (1-8)
1. Přidat soubor do obrázku
1. Nahrát soubor z obrázku
1. Vypsat obsah obrázku podle zvoleného setřídění
1. Uložit modifikovaný obrázek na disk
1. Smazat uložená data v obrázku

### Datové struktury

Za účelem rozpoznání obrázku se skrytými daty implementoval "klíč", specifický řetezec, kterým bude začínat každý upravený obrázek. Jeho úkolem je minimalizovat snahu o čtení souborů, které v sobě nic neschovávají.
Aby bylo možné ukládat více souborů do jednoho obrázku, tak jsem využil primitivní implementace link-listu. Kdy každý zápis začíná hodnoutou typu Int odkazující se na index Bytu dalšího záznamu.

Formát zápisu:
 - klíč
 - n-krát rámec uložených dat:
 	1. Int - index Bytu následujícího rámce
	1. Int - délka hlavičky
	1. Int - délka dat
	1. Int - délka názvu souboru
	1. byte[] - znaky názvu
	1. Int - délka přípony
	1. byte[] - znaky přípony
	1. byte[] - binární data souboru

### Objektový model

![Class diagram](/assets/images/class_diagram.png)

## Externí knihovna

Jako externí knohovnu jsem zvolil JUnit testy (které jsou paradoxně již integrovaným nástrojem vývojového prostředí NeatBeans).
Testovací funkce se dají automaticky generovat pomocí: Tools -> Create Tests.

Zde je vidět struktura testů:
![JUnit tests](/assets/images/JUnit_tests.png)

## Testování
Testování probíhá automaticky pomocí JUnit testů. Zde je ilustrační výpis:![JUnit tests](/assets/images/CounterTest.png)
![JUnit tests](/assets/images/DataFileTest.png)
![JUnit tests](/assets/images/FormatPPM_test.png)
![JUnit tests](/assets/images/RA_test.png)

