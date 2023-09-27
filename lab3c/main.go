package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func fillArrayWithRandomNumbers(size, maxEnergy int) []int {
	rand.Seed(time.Now().UnixNano())
	energy := make([]int, size)
	for i := 0; i < size; i++ {
		energy[i] = rand.Intn(maxEnergy + 1)
	}
	return energy
}

func duel(a, b int) int {
	fmt.Printf("бореться %d проти %d...\n", a, b)
	if a > b {
		fmt.Printf("%d перемагає %d\n", a, b)
		return a
	}
	fmt.Printf("%d перемагає %d\n", b, a)
	return b
}

func findWinner(energy []int) int {
	for len(energy) > 1 {
		var wg sync.WaitGroup
		newEnergy := []int{}
		for i := 0; i < len(energy); i += 2 {
			if i+1 < len(energy) {
				wg.Add(1)
				go func(a, b int) {
					defer wg.Done()
					result := duel(energy[a], energy[b])
					newEnergy = append(newEnergy, result)
				}(i, i+1)
			} else {
				newEnergy = append(newEnergy, energy[i])
			}
		}
		wg.Wait()
		energy = newEnergy
	}
	return energy[0]
}

func main() {

	arraySize := 16
	maxEnergy := 50

	energy := fillArrayWithRandomNumbers(arraySize, maxEnergy)

	winner := findWinner(energy)

	fmt.Printf("Переможець має %d одиниць енергії.\n", winner)
}
