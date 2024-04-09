# Project Euler: Poker Hands
This project is a solution to [problem 54: poker hands](https://projecteuler.net/problem=54) by project eula using Java
and OOP code.

## Solution
The project utilises 3 main classes :
- Card -> Contains information about a card and methods to methods used to interact with a card
- Player -> Contains utility methods for handling a hand and interacting with cards in a hand
- Play -> Initialises the game and prints the winner after reading all lines in poker.txt file

## Weaknesses of the code
- Based on the descriptions in project Euler, the code makes a number of assumptions e.g, the data provided is always
error free and clean, each player has only 5 cards e.t.c This means that in contexts without these conditions, further 
checks should be added to enhance robustness.
- Naming may (intentionally) differ from the description provided in project euler e.g. hand(Project Euler) to Player
and Card suits(Project euler) to card type. This was done to provide a more vivid understanding of the game stemming from
unfamiliarity with the poker dialect

## New Approaches
While most of the technologies used are not new to me, the approach to OOP is a bit modified from what I usually do.
For instance, instead of POJOs having getter and setter methods with util/service classes manipulating the data, classes
have self-complete methods(with no interfaces) that do something with the data. The classes only expose methods that are
required for interaction with other classes. Classes do not function as data stores rather as a node in the domain