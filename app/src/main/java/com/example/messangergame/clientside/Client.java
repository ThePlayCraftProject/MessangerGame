package com.example.messangergame.clientside;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.messangergame.AppClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

public class Client {
	private static Socket socket;
	private static Listener client;
	private static boolean playing = false;
	public volatile static ArrayDeque<String> msgs = new ArrayDeque<String>();

	private String get() {
	    String msg;
	    synchronized (msgs) {
            if (msgs.size() == 0) {
                try {
                    msgs.wait();
                } catch (InterruptedException e) {
                    send(e.toString());
                }
            }
            msg = msgs.poll();
        }
	    return msg;
    }

    protected static void send(String msg) {
        AppClient.sendToAppMessage(msg);
    }


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void start() {
	    playing = true;
		send("Clientside");
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
		    send("Введите ip адрес сервера:");
			//String ip = get();
			if (AppClient.ip.toLowerCase().equals("marvel")) AppClient.ip = "178.140.215.233";
			socket = new Socket(AppClient.ip, AppClient.port);
		} catch (IOException e) {
			send(e.toString());
			if (client != null) {
				client.askToStop();
			}
		}
		client = new Listener(socket);
		client.start();


		try(DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
		    if (!socket.isClosed() && AppClient.name != null && !AppClient.name.equals("") && playing) {
		        dos.writeUTF("/changenick "+AppClient.name);
		        dos.flush();
            }

			while(!socket.isClosed() && playing) {
                String cmd = get();
                if (cmd != null) {
                    if (cmd.toLowerCase().equals("/help")) {
                        send("Клиентские команды:");
                        send("/onlymenu - глушитель чата, при отключении отобразит пропущенные сообщения");
                    }
                    if (cmd.toLowerCase().equals("/onlymenu")) {
                        if (client.isMenuMode()) {
                            client.MenuModeOff();
                            send("Чат заглушён. Если хотите разглушить, напишите ещё раз.");
                        } else {
                            client.MenuModeOn();
                            send("Чат разглушён.");
                        }
                    } else {
                        send(cmd);
                        dos.writeUTF(cmd);
                        if (msgs.size() == 0) {
                            dos.flush();
                        }
                    }
                }
            }
		} catch (IOException e) {
            send(e.toString());
		}
	}

    public static void stop() {
	    if (playing) {
	        if (client != null) {
                client.askToStop();
            }
            playing = false;
	        if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    send(e.toString());
                }
            }
            synchronized (msgs) {
                msgs.notifyAll();
            }
        }
    }
}

class Listener extends Thread {
	private Socket socket;
	private boolean needToStop = false;
	private List<String> needToShow = new ArrayList<String>();
	private boolean menuMode = false;

	private void send(String msg) {
	    Client.send(msg);
    }


	public Listener(Socket socket) {
		this.socket = socket;
	}

	public boolean isMenuMode() {
		return menuMode;
	}
	public void MenuModeOn() {
		menuMode = true;
	}
	public void MenuModeOff() {
		menuMode = false;
		for (String s : needToShow) {
			send("<"+s);
		}
	}

	protected void info(String s) {
		System.out.println("Listener>"+s);
	}

	public void askToStop() {
		needToStop = true;
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public void run() {
		super.run();


		try(DataInputStream dis = new DataInputStream(socket.getInputStream())) {
			while (!socket.isClosed() && !needToStop) {
				boolean menu = false;
				String msg = dis.readUTF();
				if (msg.indexOf("$MENU$") == 0) {
					msg = msg.replace("$MENU$", "");
                    send("<"+msg);
				} else if (menuMode){
					needToShow.add(msg);
				} else send("<"+msg);
			}
		} catch (IOException e) {
            send(e.toString());
		}
	}
}
