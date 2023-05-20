import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Connessione {
public static void main(String[] args) throws Exception {
	
		
			Scanner tastiera = new Scanner(System.in);
			int cmd;
			String nomeAutore;
	while(true) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/Library_db";
			
			Connection con = DriverManager.getConnection(url,"library_user","1234");
			System.out.println("Connesione OK\n");
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2020);
			cal.set(Calendar.MONTH, Calendar.FEBRUARY);
			cal.set(Calendar.DATE, 20);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			java.sql.Date d = new java.sql.Date(cal.getTimeInMillis());
			
			
			System.out.println("0:ESCI");
			System.out.println("1:Stampa lista libri");
			System.out.println("2:Ricerca libro per scrittore(nome + cognome)");
			System.out.println("3:Stampa la lista dei clienti registrati");
			System.out.println("4:Numero di libri noleggiati da un cliente");
			System.out.println("5:Lista delle enciclopedie disponibili");
			System.out.println("6:Numero di enciclopedie usate da un cliente" );		
			System.out.println("7:Nome e cognome e la data in cui un cliente ha usato un'enciclopedia");
			System.out.println("8:Nome libro e data in cui un cliente ha noleggiato un libro");
			System.out.println("9:I libri acquistati da un impiegato");
			System.out.println("10:registra un nuovo cliente");
			System.out.println("11:Elimina un cliente dalla lista");
			System.out.println("12:Effettua un noleggio");
			System.out.println("13:Elimina un noleggio, causa restituzione libro");
			System.out.println("14:Nuovo evento di uso enciclopedia");
			System.out.println("15:Quali sono i libri non noleggiati al momento dal cliente");
			
			cmd = tastiera.nextInt();
			
			switch(cmd) {
			
			case 0: return;
			
			case 1:	Statement st1 = con.createStatement();
					ResultSet rs1 = st1.executeQuery("SELECT nome, SSN FROM libro");
					while(rs1.next()) {
						System.out.println(rs1.getString("nome")+ "	-->	" + rs1.getInt("SSN"));
					}
					rs1.close();
					st1.close();
					break;
					
			case 2:	System.out.println("Autori disponibili...fa la tua scelta\n");
					Statement aut = con.createStatement();
					ResultSet autRS = aut.executeQuery("SELECT scrittore FROM libro");
					int i = 0;
					while(autRS.next()) {
						System.out.print(autRS.getString("scrittore") + " -- ");
						i++;
						if(i%5==0) {
							System.out.println();
						}
					}
					autRS.close();
					aut.close();
					
					System.out.println();
					System.out.println("Inserisci solo il nome");
					String nome = tastiera.next();
					if((nome.charAt(0)>='a') && (nome.charAt(0)<='z')) {
						nome = ((char)(nome.charAt(0)-32)) + nome.substring(1,nome.length());
					}
					
					System.out.println("Inserisci solo il cognome");
					String cognome = tastiera.next();
					if((cognome.charAt(0)>='a') && (cognome.charAt(0)<='z')) {
						cognome = ((char)(cognome.charAt(0)-32)) + cognome.substring(1,cognome.length());
					}
					
					String nomeSpazio = nome.concat(" ");
					nomeAutore = nomeSpazio.concat(cognome);
					System.out.println();
					
					String sql = "SELECT nome, SSN FROM libro WHERE scrittore = ?";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, nomeAutore);
					ResultSet rs = ps.executeQuery();
					
					while(rs.next()) {
						System.out.println(rs.getString("nome")+ " " + rs.getInt("SSN"));
					}
					
					rs.close();
					ps.close();
					break;
					
			case 3: Statement st2 = con.createStatement();
					ResultSet rs2 = st2.executeQuery("SELECT nome, CF FROM cliente");
					while(rs2.next()) {
						System.out.println(rs2.getString("nome") + " " + rs2.getString("CF"));
					}
					rs2.close();
					st2.close();
					break;
					
			case 4: String sql1 = "SELECT COUNT(*) AS num FROM libro,cliente,noleggia WHERE noleggia.cliente_cf = cliente.CF AND "
					+ "noleggia.lib_S = libro.SSN AND cliente.nome = ?";
			
					System.out.println("Ecco l'elenco dei clienti");
					Statement st21 = con.createStatement();
					ResultSet rs21 = st21.executeQuery("SELECT nome, CF FROM cliente");
					int k = 0;
					while(rs21.next()) {
						System.out.print(rs21.getString("nome") + "  -- ");
						k ++;
						if (k%4==0) {
							System.out.println();						}
					}
					rs21.close();
					st21.close();
					
					System.out.println();
					System.out.println("Inserisci il nome del cliente");
					String nomeCliente = tastiera.next();
					PreparedStatement ps1 = con.prepareStatement(sql1);
					ps1.setString(1, nomeCliente);
					ResultSet rs3 = ps1.executeQuery();
					
					while(rs3.next()) {
						System.out.println(rs3.getInt("num"));
					}
					rs3.close();
					ps1.close();
					break;
					
			case 5: Statement enc1 = con.createStatement();
					ResultSet encRS = enc1.executeQuery("SELECT argomento, codiceE FROM enciclopedia");
					while(encRS.next()) {
						System.out.println(encRS.getString("argomento")+ " " + encRS.getInt("codiceE"));
					}
					encRS.close();
					enc1.close();
					break;
					
			case 6: String numeroUsi = "SELECT COUNT(*) AS NumeroUsi FROM enciclopedia AS e, usufruisce AS u, cliente AS c WHERE u.cliente_cf = c.CF AND u.enc_cod = e.codiceE AND c.nome = ?";
					PreparedStatement enc2 = con.prepareStatement(numeroUsi);
					
					System.out.println("Ecco l'elenco dei clienti");
					Statement st22 = con.createStatement();
					ResultSet rs22 = st22.executeQuery("SELECT nome, CF FROM cliente");
					int k2 = 0;
					while(rs22.next()) {
						System.out.print(rs22.getString("nome") + "  -- ");
						k2 ++;
						if (k2%4==0) {
							System.out.println();						}
					}
					rs22.close();
					st22.close();
					
					System.out.println();
					System.out.println("inserisci il nome del cliente");
					String nCli = tastiera.next();
					enc2.setString(1, nCli);
					ResultSet encRS2 = enc2.executeQuery();
					while (encRS2.next()) {
						System.out.println(encRS2.getInt("NumeroUsi"));
					}
					encRS2.close();
					enc2.close();
					break;
			
			case 7: String op7 = "SELECT c.nome, c.cognome, u.data_uso FROM cliente AS c, enciclopedia AS e, usufruisce AS u WHERE u.cliente_cf = c.CF AND u.enc_cod = e.codiceE AND e.argomento = ?";
					PreparedStatement op71 = con.prepareStatement(op7);
					System.out.println("Enciclopedie nel sistema");
					Statement enc3 = con.createStatement();
					ResultSet encRS3 = enc3.executeQuery("SELECT argomento FROM enciclopedia");
					while(encRS3.next()) {
						System.out.println(encRS3.getString("argomento") + " -- ");
					}
					encRS3.close();
					enc3.close();
					
					System.out.println("Inserisci l'argomento dell'enciclopedia");
					String enciclopedia = tastiera.next();
					op71.setString(1, enciclopedia);
					ResultSet op7rs = op71.executeQuery();
					while(op7rs.next()) {
						System.out.println("nome: " + op7rs.getString("c.nome") + " cognome: " + op7rs.getString("c.cognome") + " data uso: " + op7rs.getString("u.data_uso"));
					}
					op7rs.close();
					op71.close();
					break;
					
					
			case 8: String op8 = "SELECT l.nome, n.data_noleggio FROM libro AS l, noleggia AS n, cliente AS c WHERE n.cliente_cf = c.CF AND n.lib_S = l.SSN AND c.nome = ?";
					PreparedStatement op81 = con.prepareStatement(op8);
					
					System.out.println("Ecco l'elenco dei clienti");
					Statement st23 = con.createStatement();
					ResultSet rs23 = st23.executeQuery("SELECT nome, CF FROM cliente");
					int k3 = 0;
					while(rs23.next()) {
						System.out.print(rs23.getString("nome") + "  -- ");
						k3 ++;
						if (k3%4==0) {
							System.out.println();						}
					}
					rs23.close();
					st23.close();
					
					System.out.println();
					
					System.out.println("Inserisci solo il nome del cliente");
					String clienteNome = tastiera.next();
					op81.setString(1, clienteNome);
					ResultSet op8rs = op81.executeQuery();
					while(op8rs.next()) {
						System.out.println("nome libro: " + op8rs.getString("l.nome") + " data: " + op8rs.getString("n.data_noleggio"));
					}
					op8rs.close();
					op81.close();
					break;
					
			case 9: String op9 = "SELECT l.nome, l.ssn FROM libro AS l, impiegato AS i, acquista AS a WHERE a.imp_cod = i.codiceI AND a.lib_S = l.SSN AND i.nome = ?";
					PreparedStatement op91 = con.prepareStatement(op9);
					System.out.println("Gli impiegati sono: ");
					Statement st09 = con.createStatement();
					ResultSet rs09 = st09.executeQuery("SELECT nome FROM impiegato");
					int j = 0;
					while(rs09.next()) {
						System.out.print(rs09.getString("nome") + "	");
						j++;
						if (j%3==0) {
							System.out.println();
						}
					}
					rs09.close();
					st09.close();
					
					System.out.println();
					System.out.println("Inserisci il nome dell'impiegato");
					String impN = tastiera.next();
					op91.setString(1, impN);
					ResultSet op9rs = op91.executeQuery();
					while(op9rs.next()) {
						System.out.println("nome libro: "+ op9rs.getString("l.nome") + " SSN: " + op9rs.getInt("l.SSN"));
					}
					op9rs.close();
					op91.close();
					break;
					
			case 10:String op10 = "INSERT INTO cliente(CF, nome, cognome) VALUES (?, ?, ?)";
					PreparedStatement op101 = con.prepareStatement(op10);
					
					String cf1 = null;
					while(true) {
						System.out.println("Inserisci codice fiscale (16 caratteri alfanumerici)");
						cf1 = tastiera.next();
						if(cf1.length() == 16) {
							break;
						}
					}
					
					String cf = cf1.toUpperCase();
					System.out.println("Inserisci il nome");
					String nomeUt = tastiera.next();
					System.out.println("Inserisci il cognome");
					String cognomeUt = tastiera.next();
				
					op101.setString(1, cf);
					op101.setString(2, nomeUt);
					op101.setString(3, cognomeUt);
					op101.executeUpdate();
					System.out.println("Operazione effettuata con successo!!!");
					
					op101.close();
					break;
					
					
			case 11: String op11 = "DELETE FROM cliente WHERE CF = ?";
					 PreparedStatement op111 = con.prepareStatement(op11);
					 
					 Statement st11 = con.createStatement();
					 ResultSet rs11 = st11.executeQuery("SELECT nome, CF FROM cliente");
					 System.out.println("NOME		CF");
						while(rs11.next()) {
							System.out.println(rs11.getString("nome") + " " + rs11.getString("CF"));
						}
						rs11.close();
						st11.close();
					System.out.println("Inserisci il CF del cliente da eliminare");
					String cfDel1 = tastiera.next();
					String cfDel = cfDel1.toUpperCase();
					op111.setString(1, cfDel);
					op111.executeUpdate();
					System.out.println("Operazione effettuata con successo!!!");
					
					op111.close();
					break;
				
			case 12: String op12 = "INSERT INTO noleggia(data_noleggio, cliente_cf, lib_S) VALUES (?, ?, ?)";
					 PreparedStatement op121 = con.prepareStatement(op12);
					 
					 Statement st24 = con.createStatement();
					 ResultSet rs24 = st24.executeQuery("SELECT nome, CF FROM cliente");
					 while(rs24.next()) {
							System.out.println(rs24.getString("nome"));
					 }
					 rs24.close();
				     st24.close();
					 
					 System.out.println("### Il cliente è nella lista (SI == 1 // NO == 0)");
					 int risposta = tastiera.nextInt();
					 if(risposta == 0) {
						 System.out.println("#### --- PROCEDI CON LA REGISTRAZIONE --- ###");
						 String op105 = "INSERT INTO cliente(CF, nome, cognome) VALUES (?, ?, ?)";
							PreparedStatement op1015 = con.prepareStatement(op105);
							
							String cf15 = null;
							while(true) {
								System.out.println("Inserisci codice fiscale (16 caratteri alfanumerici)");
								cf15 = tastiera.next();
								if(cf15.length() == 16) {
									break;
								}
							}
							
							String cf6 = cf15.toUpperCase();
							System.out.println("Inserisci il nome");
							String nomeUt6 = tastiera.next();
							System.out.println("Inserisci il cognome");
							String cognomeUt6 = tastiera.next();

						
							op1015.setString(1, cf6);
							op1015.setString(2, nomeUt6);
							op1015.setString(3, cognomeUt6);
							op1015.executeUpdate();
							
							
							System.out.println("### --- REGISTRAZIONE EFFETTUATA --- ###");
							
							op1015.close();
					 }
					 
					 
					 Statement op12b = con.createStatement();
					 System.out.println("NOME		" + "	CF		");
					 ResultSet op12brs = op12b.executeQuery("SELECT nome, CF FROM cliente");
					 while(op12brs.next()) {
						 System.out.println(op12brs.getString("nome") + "		" + op12brs.getString("CF"));
					 }
					 op12brs.close();
					 op12b.close();
					
					 String cf2 = null;
						while(true) {
							System.out.println("###	Inserisci codice fiscale (16 caratteri alfanumerici) ###");
							cf2 = tastiera.next();
							if(cf2.length() == 16) {
								break;
							}
						}
						
						Statement st12 = con.createStatement();
						ResultSet rs12 = st12.executeQuery("SELECT nome, SSN FROM libro");
						
						while(rs12.next()) {
							System.out.println(rs12.getString("nome") + " " + rs12.getInt("SSN"));
						}
						rs12.close();
						st12.close();
						System.out.println("### Inserisci SSN ###");
						int SSN = tastiera.nextInt();
						
						System.out.println("###inserisci la data (AAAA-MM-DD) ###");
						String data = tastiera.next();
						java.sql.Date d2 = java.sql.Date.valueOf(data);
						 
						
						op121.setDate(1, d2);
						op121.setString(2, cf2);
						op121.setInt(3, SSN);
						op121.executeUpdate();
						
						op121.close();
						
						String op12bis = "UPDATE noleggia SET ritiro = data_noleggio + interval 2 month where data_noleggio = ? AND cliente_cf = ?";
						PreparedStatement op12bis1 = con.prepareStatement(op12bis);
						op12bis1.setDate(1, d2);
						op12bis1.setString(2,cf2);
						op12bis1.executeUpdate();
						op12bis1.close();
						
						System.out.println("Operazione effettuata con successo!!!");
						break;
			
			case 13: String op13b = "SELECT n.id, c.nome, l.nome, n.data_noleggio FROM noleggia AS n, libro AS l, cliente AS c "
									+ "WHERE n.cliente_cf = c.CF AND n.lib_S = l.SSN "
									+ "AND c.nome = ?";
					 PreparedStatement op131b = con.prepareStatement(op13b);
					 
					 Statement st32 = con.createStatement();
					 ResultSet rs32 = st32.executeQuery("SELECT nome FROM cliente");
					 int k32 = 0;
					 while(rs32.next()) {
						 System.out.print(rs32.getString("nome") + "  /---/ ");
						 k32 ++;
						 if(k32%4==0) {
							 System.out.println();
						 }
					 }
					 rs32.close();
					 st32.close();
					 
					 System.out.println();
					 System.out.println("Inserisci nome cliente per vedere i dati dei noleggi");
					 String rientro = tastiera.next();
					 op131b.setString(1, rientro);
					 ResultSet op13brs = op131b.executeQuery();
					 while(op13brs.next()) {
						 System.out.println(op13brs.getInt("n.id") + "	" + op13brs.getString("c.nome") + "	" + op13brs.getString("l.nome") + "	"+ 
								"	" + op13brs.getString("n.data_noleggio"));
					 }
					 op13brs.close();
					 op131b.close();
					 
					 String op13 = "DELETE FROM noleggia WHERE id = ?";
					 PreparedStatement op131 = con.prepareStatement(op13);
					 System.out.println("Inserisci l' id del noleggio da eliminare");
					 int idNoleggio = tastiera.nextInt();
					 op131.setInt(1, idNoleggio);
					 op131.executeUpdate();
					 
					 op131.close();
					 System.out.println("### Operazione avvenuta con successo!!! ###");
					 
					 break;
			
			
			case 14: String op14 = "INSERT INTO usufruisce (data_uso, cliente_cf, enc_cod) VALUES (?, ?, ?)";
					 PreparedStatement op141 = con.prepareStatement(op14);
						
					 Statement st14 = con.createStatement();
					 ResultSet rs14 = st14.executeQuery("SELECT nome FROM cliente");
					 int z14 = 0;
					 while(rs14.next()) {
						 System.out.print(rs14.getString("nome") + " --- ");
						 z14 ++;
						 if(z14%4 == 0) {
							 System.out.println();
						 }
							 
					 }
					 System.out.println();
					 System.out.println("### Il cliente è nella lista (SI == 1 // NO == 0)");
					 int risposta6 = tastiera.nextInt();
					 if(risposta6 == 0) {
					 System.out.println("#### --- PROCEDI CON LA REGISTRAZIONE --- ###");
					 String op1056 = "INSERT INTO cliente(CF, nome, cognome) VALUES (?, ?, ?)";
				 	 PreparedStatement op10156 = con.prepareStatement(op1056);
					
					 String cf156 = null;
					 while(true) {
						 System.out.println("Inserisci codice fiscale (16 caratteri alfanumerici)");
						 cf156 = tastiera.next();
						 if(cf156.length() == 16) {
							 break;
						 }
					 }
					
					 String cf66 = cf156.toUpperCase();
					 System.out.println("Inserisci il nome");
					 String nomeUt66 = tastiera.next();
					 System.out.println("Inserisci il cognome");
					 String cognomeUt66 = tastiera.next();
				
					 op10156.setString(1, cf66);
					 op10156.setString(2, nomeUt66);
					 op10156.setString(3, cognomeUt66);
					 op10156.executeUpdate();
					 System.out.println("### --- REGISTRAZIONE EFFETTUATA --- ###");
					
					 op10156.close();
					 }
					 
					 Statement op12b6 = con.createStatement();
					 System.out.println("NOME		" + "	CF");
					 ResultSet op12brs6 = op12b6.executeQuery("SELECT nome, CF FROM cliente");
					 while(op12brs6.next()) {
						 System.out.println(op12brs6.getString("nome") + "		" + op12brs6.getString("CF"));
					 }
					 op12brs6.close();
					 op12b6.close();
					
					 String cf26 = null;
						while(true) {
							System.out.println("###	Inserisci codice fiscale (16 caratteri alfanumerici) ###");
							cf26 = tastiera.next();
							if(cf26.length() == 16) {
								break;
							}
						}
					
					Statement en2 = con.createStatement();
					System.out.println("ARGOMENTO	" + "		" + "	CODICE");
					ResultSet en2rs = en2.executeQuery("SELECT argomento, codiceE FROM enciclopedia");
					while(en2rs.next()) {
						System.out.println(en2rs.getString("argomento") + "	" + en2rs.getInt("codiceE"));
					}
					en2rs.close();
					en2.close();
					
					System.out.println("Inserisci codice enciclopedia");
					int codice = tastiera.nextInt();
					System.out.println("###inserisci la data (AAAA-MM-DD) ###");
					String data2 = tastiera.next();
					java.sql.Date d3 = java.sql.Date.valueOf(data2);
					
					op141.setDate(1, d3);
					op141.setString(2, cf26);
					op141.setInt(3, codice);
					op141.executeUpdate();
					
					op141.close();
					System.out.println("### Operazione avvenuta con successo!!! ###");
					
					break;
					
			case 15: Statement st14bis = con.createStatement();
			 		 ResultSet rs14bis = st14bis.executeQuery("SELECT nome FROM cliente");
			 		 int z14bis = 0;
			 		 while(rs14bis.next()) {
			 			 System.out.print(rs14bis.getString("nome") + " --- ");
			 			 z14bis ++;
			 			 if(z14bis%4 == 0) {
			 				 System.out.println();
			 			 }
					 
			 		 }
			 		System.out.println();
			 		 System.out.println("Scegli il nome");
			 		 String nomeEsc = tastiera.next();
			 		 String op15 = "SELECT l.nome FROM libro AS l WHERE NOT EXISTS (SELECT *  FROM cliente AS c, noleggia AS n WHERE n.cliente_cf = c.CF AND l.SSN = n.lib_S AND c.nome = ?);";
					 PreparedStatement op151 = con.prepareStatement(op15);
					 op151.setString(1, nomeEsc);
					 ResultSet op15rs = op151.executeQuery();
					 while(op15rs.next()) {
						 System.out.println(op15rs.getString("l.nome"));
					 }
			 		 
			 		 st14bis.close();
			 		 rs14bis.close();
			 		 op151.close();
			 		 op15rs.close();

			 		 break;
			
			default: System.out.println("###################################");
					 System.out.println("Comando non valido");
					 System.out.println("###################################");
					 break;
			}
			
			
			
			con.close();
			System.out.println("\nConnessione chiusa");
			System.out.println("--------------------");
			
			
			System.out.println("################################################################################");
			System.out.println("################################# NUOVA SCELTA #################################");
		}
			
		catch(ClassNotFoundException e) {
			System.out.println("Db driver not found");
			System.out.println(e);
		}
		catch(Exception e) {
			System.out.println("Connessione fallita");
			System.out.println(e);
			}
	}

		}	
}
