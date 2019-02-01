package br.com.partnergroup.robo.test.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.partnergroup.robo.test.bd.H2Server;

public class App {

	public static void main(String[] args) throws SQLException, Exception {

		// Declaração da String para acessar o site
		String link = "http://www.solhorticenter.com.br/ofertas/";

		// Declaração da String para acessar o banco
		String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

		try (H2Server server = new H2Server()) {

			server.start();

			// Método Jsou.connect é responsável por acessar o link do site e salvar na
			// varíavel doc do tipo Document usada para guardar o conteúdo html
			Document doc = Jsoup.connect(link).get();

			// Responsável por pegar os elementos dentro da tag <body>
			Elements elementosTag = doc.select("body");

			System.out.println("Elementos com a tag <body>:");

			// for para percorrer todas as tags e um println para fins de teste no console
			for (Element el : elementosTag)
				System.out.println(el.text());

			// Conversão do elementosTag para Texto para set no banco
			String Texto = elementosTag.text().toString();
			String validaTEXTO = Texto;

			// bloco com try/catch para tratarmos as exceções
			// conectamos com o banco de dados e preparamos a conexao para inserir no banco
			try (Connection con = DriverManager.getConnection(url, "sa", "")) {
				try (PreparedStatement ps = con.prepareStatement("INSERT INTO tb_html(html) VALUES(?)")) {
					ps.setString(1, validaTEXTO);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "Clique em ok para finalizar");

			}
		}
	}
}
