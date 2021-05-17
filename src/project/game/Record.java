package project.game;

import java.io.Serializable;

class Record implements Serializable
{
	String name;
	int score;

	Record(String name, int score) {
		this.name = name;
		this.score = score;
	}
}
