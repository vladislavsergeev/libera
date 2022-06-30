package socketmaster;

import utils.Const;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.ByteBuffer;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Socketmaster implements Runnable {
	static Connection con = null;
	
	Thread t;
	Socketmaster next;
	
	ServerSocket ss;
	Socket s;
	InputStream in;
	OutputStream out;
	
	int user_id;
	
	public Socketmaster(ServerSocket _ss) {
        ss = _ss;
		
		t = new Thread(this, "Socketmaster thread");
        t.start();
    }
	
	private void init() {
		try {
            System.out.println("Waiting connection...");
			s = ss.accept();
			
			System.out.println("Local port: " +  s.getLocalPort());	
			System.out.println("Remote port: " + s.getPort() + '\n');
			
			in = s.getInputStream();
			out = s.getOutputStream();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
	}
	
	public boolean send(int sended) {
        byte[] buf = ByteBuffer.allocate(4).putInt(sended).array();
        try {
            out.write(buf);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public boolean send(double sended) {
        byte[] buf = ByteBuffer.allocate(24).putDouble(sended).array();
        try {
            out.write(buf);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public boolean send(String sended) {
        try {
            out.write(sended.getBytes());
            return true;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public int waitForInt() {
        try {
            byte[] buf2 = new byte[2000];
            int count = in.read(buf2);
            ByteBuffer bb = ByteBuffer.wrap(buf2, 0, count);
            int res = bb.getInt();
            return res;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return -1;
        }
    }

    public double waitForDouble() {
        try {
            byte[] buf2 = new byte[2000];
            int count = in.read(buf2);
            ByteBuffer bb = ByteBuffer.wrap(buf2, 0, count);
            double res = bb.getDouble();
            return res;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return -1;
        }
    }

    public String waitForString() {
        try {
            byte[] buf = new byte[2000];
            int count = in.read(buf);
            String res = new String(buf, 0, count);
            return res;
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return "NULL";
        }
    }

    public void run() {
        try {
			init();
			
			next = new Socketmaster(ss);
		   
			boolean stop = false;
		   
			while (!stop) {
				int qu = waitForInt();
				
				if (qu == Const.LOGIN) {
					send(1);
					
					String login = waitForString();
					send(1);
					
					String password = waitForString();
					
					String sqlString;

					sqlString = "SELECT * FROM users WHERE login = '" + login + "'";
					
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}

					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						ResultSet resultSet = prepareStatement.executeQuery();
						if (resultSet.next()) {
							if (resultSet.getString("password").equals(password)) {
								send(Const.SUCCESS);
								user_id = resultSet.getInt("id");
							} else send(Const.WRONG_PASSWORD);
						}
						else send(Const.LOGIN_DENIED);
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
				}
				else if (qu == Const.REGISTRATION) {
					send(1);
					
					String login = waitForString();
					send(1);
					
					String password = waitForString();
					
					String sqlString;

					sqlString = "SELECT * FROM users WHERE login = '" + login + "'";

					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}

					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						ResultSet resultSet = prepareStatement.executeQuery();
						if (resultSet.next()) send(Const.LOGIN_DENIED);						
						else {
							send(Const.SUCCESS);
							sqlString = "INSERT INTO users (login,password) values (?,?)";
							try {
								Class.forName("org.hsqldb.jdbcDriver");
								con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
							} catch (Exception ex) {
								System.out.println(ex);
							}
							try {
								prepareStatement = con.prepareStatement(sqlString);

								prepareStatement.setString(1, login);
								prepareStatement.setString(2, password);
								prepareStatement.execute();
								System.out.println("insert Done ...");							
							} catch (SQLException ex) {
								System.out.println(ex);
								System.out.println("failed to insert table");
							}
						}
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
					
					sqlString = "SELECT * FROM users WHERE login = '" + login + "'";
					
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}

					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						ResultSet resultSet = prepareStatement.executeQuery();
						if (resultSet.next()) {
							user_id = resultSet.getInt("id");
						}
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
				}
				else if (qu == Const.ADD) {
					send(1);
					
					String text = waitForString();
					send(1);
					
					String color = waitForString();
					send(1);
					
					String sqlString = "INSERT INTO tasks (text,color,user_id) values (?,?,?)";
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}
					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);

						prepareStatement.setString(1, text);
						prepareStatement.setString(2, color);
						prepareStatement.setInt(3, user_id);
						prepareStatement.execute();
						System.out.println("insert Done ...");
					} catch (SQLException ex) {
						System.out.println(ex);
						System.out.println("failed to insert table");
					}
					
					con.close();
				}
				else if (qu == Const.REMOVE) {
					send(1);
					int id = waitForInt();
					
					String sqlString = "DELETE FROM tasks WHERE id =  ?";
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}
					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						prepareStatement.setInt(1, id);
						prepareStatement.executeUpdate();
					} catch (SQLException ex) {
						System.out.println(ex.getMessage());
						System.out.println("failed to delete the table");
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
					
					send(1);
				}
				else if (qu == Const.EDIT) {
					send(1);
					
					String text = waitForString();
					send(1);
					
					int id = waitForInt();
					
					String sqlString = "Update tasks set text = ? where id = ?";
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}
					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						prepareStatement.setInt(2, id);
						prepareStatement.setString(1, text);
						prepareStatement.executeUpdate();
					} catch (SQLException ex) {
						System.out.println(ex);
						System.out.println("failed to insert in table");
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
				}
				else if (qu == Const.GET) {
					String sqlString = "SELECT * FROM tasks where user_id = " + user_id;
					
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}
					
					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						ResultSet resultSet = prepareStatement.executeQuery();
						while (resultSet.next()) {
							send(resultSet.getInt(1));
							waitForInt();
							
							send(resultSet.getString(2));
							waitForInt();
							
							send(resultSet.getString(3));
							waitForInt();
						}
						
						send(-1);
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
				}
				else if (qu == Const.SEARCH) {
					send(1);
					String search = waitForString();
					
					String sqlString = "SELECT * FROM tasks where user_id = " + user_id + " and text like '%" + search + "%'";
					
					try {
						Class.forName("org.hsqldb.jdbcDriver");
						con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
					} catch (Exception ex) {
						System.out.println(ex);
					}
					
					try {
						PreparedStatement prepareStatement = con.prepareStatement(sqlString);
						ResultSet resultSet = prepareStatement.executeQuery();
						while (resultSet.next()) {
							send(resultSet.getInt(1));
							waitForInt();
							
							send(resultSet.getString(2));
							waitForInt();
							
							send(resultSet.getString(3));
							waitForInt();
						}
						
						send(-1);
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						try {
							con.close();
						} catch (SQLException ex) {
							System.out.println(ex);
						}
					}
				}
			}
		   
			in.close();
			out.close();
			s.close();
		}
		catch (Exception e) {
			System.out.println("Error: " + e);	
		}
    }
}