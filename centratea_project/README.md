# Centratea - Joc client-server în Java

## 1. Descriere generală

Proiectul implementează un joc distribuit de tip client-server, asemănător jocului „Bulls and Cows”.

Serverul generează un număr format din 4 cifre distincte, iar clienții încearcă să îl ghicească. Pentru fiecare încercare, serverul răspunde cu numărul de cifre centrate și necentrate.

Când un client ghicește numărul, serverul anunță toți clienții, generează un număr nou și jocul se reia automat.

---

## 2. Regulile jocului

Numărul trebuie să aibă:

- exact 4 cifre;
- cifre distincte;
- doar cifre de la 0 la 9.

Exemplu valid:

```text
1234
```

Exemple invalide:

```text
1123
abcd
12345
```

---

## 3. Centrate și necentrate

**Centrate** = cifre corecte aflate pe poziția corectă.

**Necentrate** = cifre corecte, dar aflate pe poziție greșită.

Exemplu:

```text
Număr secret: 1234
Încercare:    1325
```

Rezultat:

```text
centrate=1 necentrate=2
```

Explicație: cifra `1` este pe poziția corectă, iar cifrele `2` și `3` există, dar sunt pe poziții greșite.

---

## 4. Comenzi trimise de client

Clientul poate trimite următoarele comenzi:

```text
CONNECT nume_client
GUESS numar
QUIT
```

Exemplu:

```text
CONNECT Ana
GUESS 1234
QUIT
```

---

## 5. Răspunsuri posibile de la server

```text
OK_CONNECTED
```

Clientul s-a conectat cu succes.

```text
ERROR_NAME_TAKEN
```

Numele este deja folosit de alt client.

```text
ERROR_INVALID_NAME
```

Numele este invalid.

```text
ERROR_INVALID_COMMAND
```

Comanda trimisă nu este recunoscută.

```text
ERROR_INVALID_GUESS
```

Încercarea este invalidă, de exemplu are cifre repetate, nu are 4 cifre sau conține alte caractere.

```text
RESULT centrate=x necentrate=y attempts=z
```

Încercarea este validă. Serverul trimite numărul de centrate, necentrate și numărul de încercări ale clientului.

```text
WIN winner=nume_client attempts=z
```

Un client a ghicit numărul. Mesajul este trimis tuturor clienților.

```text
RESET
```

Serverul a generat un număr nou și începe o rundă nouă.

```text
BYE
```

Clientul s-a deconectat.

```text
INFO mesaj
```

Mesaj informativ, de exemplu conectarea sau deconectarea unui client.

---

## 6. Scenarii de testare

### Scenariul 1 - Conectare client

Clientul trimite:

```text
CONNECT Ana
```

Serverul răspunde:

```text
OK_CONNECTED
```

---

### Scenariul 2 - Nume deja folosit

Dacă un alt client încearcă să se conecteze tot cu numele `Ana`, serverul răspunde:

```text
ERROR_NAME_TAKEN
```

---

### Scenariul 3 - Încercare invalidă

Clientul trimite:

```text
GUESS 1123
```

Serverul răspunde:

```text
ERROR_INVALID_GUESS
```

---

### Scenariul 4 - Încercare validă

Clientul trimite:

```text
GUESS 1234
```

Serverul răspunde, de exemplu:

```text
RESULT centrate=2 necentrate=1 attempts=1
```

---

### Scenariul 5 - Câștig și resetare joc

Dacă un client ghicește numărul, serverul trimite:

```text
RESULT centrate=4 necentrate=0 attempts=3
WIN winner=Ana attempts=3
RESET
```

După `RESET`, serverul generează un număr nou și jocul continuă.

---

## 7. Rulare server în Docker

Construirea imaginii Docker:

```bash
docker build -t centrate-server .
```

Pornirea serverului:

```bash
docker run --rm -p 12345:12345 centrate-server
```

Clientul se conectează la:

```text
localhost 12345
```