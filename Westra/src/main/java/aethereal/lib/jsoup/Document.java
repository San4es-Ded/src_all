package aethereal.lib.jsoup;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.select.Elements;

public final class Document {
   private final org.jsoup.nodes.Document delegate;

   Document(org.jsoup.nodes.Document delegate) {
      this.delegate = delegate;
   }

   public Element k(String cssQuery) {
      org.jsoup.nodes.Element element = this.delegate.selectFirst(cssQuery);
      return element == null ? null : new Element(element);
   }

   public List<Element> j(String cssQuery) {
      Elements elements = this.delegate.select(cssQuery);
      List<Element> result = new ArrayList<>(elements.size());

      for (org.jsoup.nodes.Element element : elements) {
         result.add(new Element(element));
      }

      return result;
   }

   public Document g() {
      return this;
   }

   public org.jsoup.nodes.Element bodyElement() {
      return this.delegate.body();
   }

   public String a_(String attributeKey) {
      org.jsoup.nodes.Element body = this.delegate.body();
      return body == null ? "" : body.attr(attributeKey);
   }
}
