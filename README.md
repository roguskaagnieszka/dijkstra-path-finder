# Wizualizator Algorytmu Dijkstry

Aplikacja w Javie z graficzną wizualizacją algorytmu Dijkstry, wykorzystująca bibliotekę **GraphStream**.

Pozwala użytkownikowi zobaczyć krok po kroku, jak działa algorytm najkrótszej ścieżki oraz wizualnie śledzić wyznaczone drogi w grafie.

---

## 🧑‍🏫 Co może robić użytkownik?

Po uruchomieniu programu użytkownik może:

- ✅ Wybrać sposób wprowadzenia danych:
    - Wczytać **przykładowy graf**
    - Wygenerować **losowy graf** z wybraną liczbą wierzchołków
    - **Samodzielnie wprowadzić graf** — podać liczbę wierzchołków i listę krawędzi z wagami
- ✅ Podać numer **wierzchołka startowego**
- ✅ Otrzymać:
    - **Wizualizację grafu** z oznaczoną najkrótszą ścieżką od wierzchołka startowego do wszystkich innych
    - **Tabelę kroków algorytmu Dijkstry** w konsoli
- ✅ Powtórzyć działanie lub zakończyć program

---

## 📊 Tabela kroków w konsoli

Po uruchomieniu algorytmu, w konsoli generowana jest tabela przedstawiająca **kolejne stany tablicy odległości i poprzedników**:

- Format komórek: `d/p`
    - `d` — bieżąca znana odległość od wierzchołka startowego
    - `p` — poprzednik w ścieżce najkrótszej
- Jeśli wierzchołek nie został jeszcze odwiedzony: `∞/-`

## 🧠 Technologie

- Java 8+
- [GraphStream](http://graphstream-project.org/) – do wizualizacji grafu
- Maven – do budowania i uruchamiania aplikacji

---

## 🚀 Jak uruchomić?

1. **Sklonuj repozytorium i przejdź do katalogu:**
   ```bash
   git clone https://github.com/twoj-login/dijkstra-path-finder.git
   cd dijkstra-path-finder

