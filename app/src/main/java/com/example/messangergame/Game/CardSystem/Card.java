package com.example.messangergame.Game.CardSystem;

public class Card {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    private int id;
    private String name;
    private int price;
    public Card(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
