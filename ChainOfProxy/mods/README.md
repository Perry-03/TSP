# 🧩 Guida all’utilizzo dei moduli Java 

Questa guida spiega come **organizzare**, **compilare** ed **eseguire** un progetto Java strutturato con moduli (`module-info.java`).  

---

## 📁 Struttura consigliata del progetto

Esempio di struttura dei file:

```
project-root/
│
├── src/
│   ├── annotations/
│   │   ├── module-info.java
│   │   └── *.java
│   │
│   ├── handlers/
│   │   ├── module-info.java
│   │   └── *.java
│   │
│   └── main/
│       ├── module-info.java
│       └── *.java
│
└── mods/   ← qui verranno salvati i file .class compilati
```

---

## 🧩 Definizione dei moduli

Ogni sottocartella di `src/` rappresenta un **modulo**.  
Ogni modulo deve contenere un file `module-info.java` che dichiara:
- quali package vengono **esportati** (`exports`)
- e da quali altri moduli **dipende** (`requires`)

### Esempi

#### Modulo `annotations`
```java
module annotations {
    exports annotations;
}
```

> Il modulo `annotations` espone il package `annotations` e **non richiede** altri moduli.

#### Modulo `handlers`
```java
module handlers {
    requires annotations;
    exports handlers;
}
```

> Il modulo `handlers` **dipende** da `annotations` e **espone** il proprio package `handlers`.

#### Modulo `main`
```java
module main {
    requires annotations;
    requires handlers;
}
```

> Il modulo `main` dipende da entrambi i moduli e contiene la classe con il metodo `main()`.

---

## ⚙️ Compilazione passo per passo

Assicurati di essere **nella root del progetto** (dove si trova la cartella `src/`).

### 1️⃣ Compila il modulo `annotations`
```bash
javac -d mods/annotations src/annotations/module-info.java src/annotations/*.java
```

### 2️⃣ Compila il modulo `handlers`
```bash
javac -d mods/handlers --module-path mods src/handlers/module-info.java src/handlers/*.java
```

### 3️⃣ Compila il modulo `main`
```bash
javac -d mods/main --module-path mods src/main/module-info.java src/main/*.java
```

> `--module-path mods` indica a `javac` dove trovare i moduli già compilati (necessari per le dipendenze).

---

## 🚀 Esecuzione del programma

Per eseguire la classe `TestProxy` (contenente il metodo `main`):

```bash
java --module-path mods -m main/main.TestProxy
```

- `--module-path mods` → specifica la directory dove sono presenti i moduli compilati  
- `-m main/main.TestProxy` → indica di eseguire la classe `TestProxy` del modulo `main`

> 🔹 Il formato corretto è `nomeModulo/nomeCompletoClasse` (non usare slash inversi `\`).

---

## 🔓 Reflection e moduli

A partire da Java 9, il **sistema dei moduli** limita l’accesso tramite *reflection* a meno che non venga esplicitamente concesso.  
Se usi `java.lang.reflect` per accedere a metodi, campi o annotazioni, potresti ricevere errori o risultati vuoti.

Puoi risolvere in due modi:

### 1️⃣ Aggiungere `opens` nel `module-info.java`
Esempio:
```java
module annotations {
    opens annotations;
    exports annotations;
}
```

> `opens` permette l’accesso riflessivo al package, mentre `exports` permette l’accesso normale (a compile-time).

### 2️⃣ Aggiungere `--add-opens` a runtime
Esempio:
```bash
java --module-path mods --add-opens annotations/annotations=handlers -m main/main.TestProxy
```

> In questo caso, `handlers` ottiene il permesso di accedere tramite reflection al package `annotations`.
**Questo sembra essere anche il metodo utilizzato dal prof**

---

## 📜 Riepilogo comandi principali

| Operazione | Comando |
|-------------|----------|
| Compilare `annotations` | `javac -d mods/annotations src/annotations/module-info.java src/annotations/*.java` |
| Compilare `handlers` | `javac -d mods/handlers --module-path mods src/handlers/module-info.java src/handlers/*.java` |
| Compilare `main` | `javac -d mods/main --module-path mods src/main/module-info.java src/main/*.java` |
| Eseguire programma | `java --module-path mods -m main/main.TestProxy` |
| Consentire reflection | `--add-opens annotations/annotations=handlers` |

---

## ⚠️ Suggerimenti utili

> È possibile che la reflection standard non funzioni, bisogna usare metodi di ``Lookup`` importando librerie come ``java.lang.invoke.MethodHandles`` e ``java.lang.invoke.MethodHandle`` come mostrato dal prof.
---
