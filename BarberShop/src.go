package main

import (
	"fmt"
	"time"
	"strconv"
	"sync"
)

var wg sync.WaitGroup

type Client struct {
	name string
}

type Barber struct {
	toDo     chan *Client
	finished chan *Client
}

func (barber *Barber) startDay() {
	for client := range barber.toDo {
		fmt.Println("Doing haircut for ", client.name)
		time.Sleep(1000)
		barber.finished <- client
		fmt.Println("Haircut is done for", client.name)
	}
	close(barber.finished)
	wg.Done()
}

func enterBarbershop(clients []Client, clientsQueue chan *Client) {
	for i := 0; i < len(clients); i++ {
		clientsQueue <- &clients[i]
		time.Sleep(200)
	}
	close(clientsQueue)
	wg.Done()
}

func main() {
	fmt.Println("BarberShop")
	n := 4

	clientsQueue := make(chan *Client, n)
	done := make(chan *Client, n)

	barber := Barber{clientsQueue, done}
	wg.Add(1)
	go barber.startDay()

	clients := make([]Client, n)
	for i := 0; i < n; i++ {
		clients[i].name = "Client #" + strconv.Itoa(i)
	}
	wg.Add(1)
	go enterBarbershop(clients, clientsQueue)

	wg.Wait()
}
