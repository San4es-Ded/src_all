package aethereal.lib.jsoup;

import java.util.function.Consumer;
import org.jsoup.select.Elements;

public final class Element {
   private final org.jsoup.nodes.Element delegate;

   Element(org.jsoup.nodes.Element delegate) {
      this.delegate = delegate;
   }

   public String ac() {
      return this.delegate.text();
   }

   public String af() {
      return this.delegate.ownText();
   }

   public String c() {
      return this.delegate.text();
   }

   public String a_(String attributeKey) {
      return this.delegate.attr(attributeKey);
   }

   public boolean b_(String attributeKey) {
      return this.delegate.hasAttr(attributeKey);
   }

   public Element k(String cssQuery) {
      org.jsoup.nodes.Element element = this.delegate.selectFirst(cssQuery);
      return element == null ? null : new Element(element);
   }

   public Element j(String cssQuery) {
      return this.k(cssQuery);
   }

   public Elements select(String cssQuery) {
      return this.delegate.select(cssQuery);
   }

   public void forEach(Consumer<? super org.jsoup.nodes.Element> action) {
      this.delegate.select("input[type=hidden]").forEach(action);
   }
}
