package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.Game;
import game.World;
import transfer.B;
import transfer.C;
import transfer.O;
import transfer.P;
import transfer.T;
import transfer.U;

public class Connection {

	public static final String SERVER_NAME = "localhost";
	public static final int SERVER_PORT = 26194;

	public ObjectOutputStream out;
	public ObjectInputStream in;

	public boolean connected = false;

	public Connection() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Connecting to " + SERVER_NAME + " on port " + SERVER_PORT);
					@SuppressWarnings("resource")
					Socket socket = new Socket(SERVER_NAME, SERVER_PORT);
					System.out.println("Just connected to " + socket.getRemoteSocketAddress());

					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());
					connected = true;

					while (true) {
						try {
							input(in.readObject());
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
							return;
						}
					}

				} catch (IOException e) {
					connected = false;
					e.printStackTrace();
					return;
				}
			}
		});
		t.start();
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			if (connected) {
				break;
			}
		}
	}

	public void input(Object a) {
		Game game = ClientMain.getGame();

		B bundle = null;
		if (!(a instanceof B)) {
			bundle = new B();
			bundle.addT((T) a);
		} else {
			bundle = (B) a;
		}

		for (T o : bundle.getBundle()) {
			switch (o.getClass().getName()) {
			case "transfer.C":
				World.setChunk(((C) o));
				break;
			case "transfer.U":
				game.getDialog().setDialog(((U) o));
				ClientMain.getFrame().getScreen().drawAll();
				break;
			case "transfer.P":
				if (game.getPlayer() == null) {
					game.start();
				}
				game.setPlayer((P) o);
			case "transfer.O":
				O obj = (O) o;
				game.getWorld().setObjectAt(obj, obj.getX(), obj.getY());
				// TODO: optimization: not always drawing whole world
				// ClientMain.getFrame().getScreen().updateObject(obj);
				break;
			default:
				System.err.println(o.getClass().getName());
				break;
			}
		}
	}

	public void output(Object o) {
		try {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
