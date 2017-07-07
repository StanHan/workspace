/**
 * 
 */
package demo.java.lang.enumeration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stan
 *
 */
public class Card {

	public static void main(String[] args) {
		int numHands = Integer.parseInt("2");
		int cardsPerHand = Integer.parseInt("3");
		List<Card> deck = Card.newDeck();
		Collections.shuffle(deck);
		for (int i = 0; i < numHands; i++) {
			System.out.println(deal(deck, cardsPerHand));
		}

	}

	private static Map<Suit, Map<Rank, Card>> table = new EnumMap<Suit, Map<Rank, Card>>(Suit.class);

	static {
		for (Suit suit : Suit.values()) {
			Map<Rank, Card> suitTable = new EnumMap<Rank, Card>(Rank.class);
			for (Rank rank : Rank.values()) {
				suitTable.put(rank, new Card(rank, suit));
			}
			table.put(suit, suitTable);
		}
	}
	
	public static Card valueOf(Rank rank, Suit suit) {
	    return table.get(suit).get(rank);
	}

	public static ArrayList<Card> deal(List<Card> deck, int n) {
		int deckSize = deck.size();
		List<Card> handView = deck.subList(deckSize - n, deckSize);
		ArrayList<Card> hand = new ArrayList<Card>(handView);
		handView.clear();
		return hand;
	}

	public enum Rank {
		DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	private final Rank rank;
	private final Suit suit;

	private Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public Rank rank() {
		return rank;
	}

	public Suit suit() {
		return suit;
	}

	public String toString() {
		return rank + " of " + suit;
	}

	private static final List<Card> protoDeck = new ArrayList<Card>();

	// Initialize prototype deck
	static {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				protoDeck.add(new Card(rank, suit));
			}
		}

	}

	public static ArrayList<Card> newDeck() {
		return new ArrayList<Card>(protoDeck); // Return copy of prototype deck
	}
}
