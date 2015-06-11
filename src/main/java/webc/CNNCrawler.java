package webc;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;

import javax.xml.bind.JAXBException;

import jaxb.Noticia;
import jaxb.Noticias;
import marshall.JAXBHandler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class CNNCrawler {

	public static void main(String[] args) throws IOException, SocketTimeoutException {
		final String cnnURL = "http://edition.cnn.com/world";
		final String cnnPrefix = "http://edition.cnn.com";
		Connection connection = Jsoup.connect(cnnURL);
		Document html = connection.get();
		Elements headlines = html.select("h3.cd__headline > a[href]");
		Noticias newsAgg = new Noticias();
		int count = 0;
		for (Element e:headlines) {
			// Saltar se não for notícia (link começar por "2015/06/08"
			if (e.attr("href").startsWith("/2015")) {
				if (count > 10) break;
				Connection c = Jsoup.connect(cnnPrefix + e.attr("href")).timeout(0);
				Document newsHTML = c.get();
				Noticia n = new Noticia();
				n.setTitulo(e.text());
				n.setData("data");
				n.setUrl(cnnPrefix + e.attr("href"));
				Elements newsEl = newsHTML.select("p.zn-body__paragraph");
				String body = "";
				for (Element el:newsEl) {
					body += el.text();
				}
				n.setCorpo(body);
				newsAgg.getNoticia().add(n);
			}
			System.out.println(count);
			count++;
		}

		try {
			JAXBHandler.marshal(newsAgg.getNoticia(), new File("news.xml"));
		} catch (JAXBException e1) {

			e1.printStackTrace();
		}


	}

}
