package de.pfeufferweb.filewatch;

public class HtmlMessageBuilder {

    final StringBuilder b;

    public HtmlMessageBuilder() {
        this(new StringBuilder());
        b.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n");
        b.append("<html>\n<body>\n");
    }

    private HtmlMessageBuilder(StringBuilder b) {
        this.b = b;
    }

    public String toString() {
        b.append("</body>\n</html>\n");
        return b.toString();
    }

    public HtmlMessageBuilder bold(String s) {
        b.append("<b>").append(s).append("</b>");
        return this;
    }

    public HtmlMessageBuilder paragraph() {
        b.append("<p>");
        return this;
    }

    public HtmlMessageBuilder link(String url, String text) {
        b.append("<a href='").append(url).append("'>").append(text).append("</a>\n");
        return this;
    }

    public ListBuilder list() {
        return new ListBuilder(b, this);
    }

    public HtmlMessageBuilder text(String s) {
        b.append(s);
        return this;
    }

    public static String simpleText(String s) {
        return new HtmlMessageBuilder().text(s).toString();
    }

    public static class ListBuilder extends HtmlMessageBuilder {

        private final HtmlMessageBuilder parentBuilder;

        public ListBuilder(StringBuilder b, HtmlMessageBuilder parentBuilder) {
            super(b);
            this.parentBuilder = parentBuilder;
            b.append("<ul>");
        }

        public HtmlMessageBuilder endList() {
            b.append("</ul>\n");
            return parentBuilder;
        }

        public ListBuilder startItem() {
            b.append("<li>");
            return this;
        }

        public ListBuilder endItem() {
            b.append("<li>");
            return this;
        }
    }
}
