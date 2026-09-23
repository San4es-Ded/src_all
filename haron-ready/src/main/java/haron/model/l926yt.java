package haron.model;

import haron.model.h3ql66;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class l926yt {
    public List<h3ql66> a = new ArrayList<h3ql66>();
    public int b = 64;
    public int c = 64;

    private h3ql66 findPart(String string, h3ql66 h3ql662) {
        if (h3ql662.d.equals(string)) {
            return h3ql662;
        }
        Iterator<h3ql66> iterator = h3ql662.b.iterator();
        while (iterator.hasNext()) {
            h3ql66 h3ql663 = this.findPart(string, iterator.next());
            if (h3ql663 == null) continue;
            return h3ql663;
        }
        return null;
    }

    public Optional<h3ql66> a(String string) {
        Iterator<h3ql66> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            h3ql66 h3ql662 = this.findPart(string, iterator.next());
            if (h3ql662 == null) continue;
            return Optional.of(h3ql662);
        }
        return Optional.empty();
    }
}

