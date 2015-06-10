package webc;

import java.io.File;
import java.io.IOException;

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

	public static void main(String[] args) throws IOException {
		final String cnnURL = "http://edition.cnn.com/world";
		final String cnnPrefix = "http://edition.cnn.com";
		Connection connection = Jsoup.connect(cnnURL);
		Document html = connection.get();
		Elements headlines = html.select("h3.cd__headline > a[href]");
		Noticias newsAgg = new Noticias();
		for (Element e:headlines) {
			Connection c = Jsoup.connect(cnnPrefix + headlines.attr("href"));
			Document newsHTML = c.get();
			Noticia n = new Noticia();
			n.setTitulo(e.text());
			n.setData("01/01/1900");
			n.setUrl(cnnPrefix + headlines.attr("href"));
			Elements newsEl = newsHTML.select("p.zn-body__paragraph");
			String body = "";
			for (Element el:newsEl) {
				body += el.text();
			}
			n.setCorpo(body);
			newsAgg.getNoticia().add(n);
		}
		
		try {
			JAXBHandler.marshal(newsAgg.getNoticia(), new File("news.xml"));
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}

}
