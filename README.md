#!/bin/bash
# Compile and run the Simple Calculator
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/calculator/Calculator.java && java -cp out calculator.Calculator
