# GuessTheAnimal_Java
A simple interactive game where the computer will try to guess the animal that the person has in mind with the help of yes or no questions. During the game, the computer will extend its knowledge base by learning new facts about animals and using this information in the next game.

## Features
- Simple command line interface
- Uses a binary tree to store information.
- Saves the data to XML, JSON or YAML to persist across sessions.
- Supports English and Esperanto (and can be easily extended to other languages.)

## Getting Started
To get a local copy of the project up and running on your machine, follow these steps:

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Gradle 8.2
- Git

### Build & Run
1. Clone the repository:
```
git clone https://github.com/Aeronaul/GuessTheAnimal_Java.git
```
```
cd GuessTheAnimal_Java
```
2. Run gradle build:
```
gradle build
```
3. Extract the generated tar/zip archive:
```
cd build/distributions
```

```
tar -xf GuessTheAnimal_Java.tar
```

```
./GuessTheAnimal_Java/bin/GuessTheAnimal_Java
```

## Acknowledgements
The Java programming language and Jackson project.

## Contact
For any questions or inquiries, please contact me at: aeronaul@proton.me.
