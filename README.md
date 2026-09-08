# genetic-examples

Three worked examples of evolutionary computation in Java, built on the [Jenetics](https://jenetics.io/) library and its `jenetics.prog` genetic-programming module.

Each example attacks a different kind of problem, and together they cover the range from a toy demonstration to a program that learns to play a game.

## The examples

### Weasel program

Dawkins' classic demonstration that cumulative selection is not the same as random chance. A population of random strings evolves towards a target phrase — here `"to be or not to be."` — through selection and mutation alone. Useful as the simplest possible illustration of why evolution converges while pure randomness does not.

### Symbolic regression

Genetic programming applied to function interpolation. Instead of fitting parameters to a fixed equation, the algorithm evolves the **expression tree itself** from a set of operations and terminals, searching for a formula that reproduces data loaded from CSV. The result is a readable mathematical expression rather than an opaque model.

### Neural Snake

A neural network plays Snake, with its weights evolved by a genetic algorithm rather than trained by backpropagation. The network starts empty and untrained; fitness is how well the resulting snake plays. After training, the best network can be watched playing in real time.

## Running

```bash
./gradlew run
```

Pick an example by uncommenting the corresponding line in `gp/Examples.java`. The Snake example runs by default.

## Built with

- [Jenetics](https://jenetics.io/) 5.2.0 — evolutionary algorithms
- `jenetics.prog` — genetic programming and expression trees
- OpenCSV, Guava

## Background

Written alongside a Master's thesis on exploring process trees with genetic programming, which used the same library.
