package io.github.no_such_company.smailclientapp.bytestream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteArrayInOutStream extends ByteArrayOutputStream {

    public ByteArrayInOutStream() {
        super();
    }

    public ByteArrayInOutStream(int size) {
        super(size);
    }

    public ByteArrayInputStream getInputStream() {
        ByteArrayInputStream in = new ByteArrayInputStream(this.buf, 0, this.count);
        this.buf = null;

        return in;
    }
}