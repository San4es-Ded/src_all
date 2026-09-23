package aethereal.discord;

import aethereal.lib.javassist.Frame;
import aethereal.lib.jsoup.Connection;
import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Generated;

public class WindowsConnection implements Connection {
   @Generated
   private static final Logger a = LogManager.b(WindowsConnection.class);
   private static final int b = 50;
   final RandomAccessFile d;
   volatile boolean g;
   private final ReentrantLock c = new ReentrantLock();
   private final InputStream e = new InputStream() {
      @Override
      public int read() throws IOException {
         byte[] b2 = new byte[1];
         int n = this.read(b2, 0, 1);
         return n == -1 ? -1 : b2[0] & 0xFF;
      }

      @Override
      public int read(byte[] b2, int off, int len) throws IOException {
         if (b2 == null) {
            throw new NullPointerException("b is marked non-null but is null");
         } else if (len == 0) {
            return 0;
         } else {
            while (!WindowsConnection.this.g) {
               try {
                  long available = WindowsConnection.this.d.length();
                  if (available > 0L) {
                     return WindowsConnection.this.d.read(b2, off, len);
                  }

                  try {
                     Thread.sleep(50L);
                  } catch (InterruptedException var7) {
                     Thread.currentThread().interrupt();
                     throw new InterruptedIOException("Read interrupted");
                  }
               } catch (IOException var8) {
                  if (WindowsConnection.this.g) {
                     return -1;
                  }

                  throw var8;
               }
            }

            return -1;
         }
      }
   };
   private final OutputStream f = new OutputStream() {
      @Override
      public void write(int b2) throws IOException {
         WindowsConnection.this.d.write(b2);
      }

      @Override
      public void write(byte[] b2, int off, int len) throws IOException {
         if (b2 == null) {
            throw new NullPointerException("b is marked non-null but is null");
         } else {
            WindowsConnection.this.d.write(b2, off, len);
         }
      }
   };

   public WindowsConnection(String path) throws IOException {
      this.d = new RandomAccessFile(path, "rw");
      a.a("Connected to Windows pipe: {}", path);
   }

   @Override
   public boolean a() {
      return !this.g;
   }

   @Override
   public Frame b() throws IOException {
      return FrameReader.a(this.e);
   }

   @Override
   public void a(Frame frame) throws IOException {
      this.c();
      this.c.lock();

      try {
         this.c();
         FrameWriter.a(this.f, frame);
      } finally {
         this.c.unlock();
      }
   }

   @Override
   public void close() throws IOException {
      if (!this.g) {
         this.g = true;
         this.d.close();
         a.a("Windows connection closed");
      }
   }

   private void c() throws IOException {
      if (this.g) {
         throw new IOException("Connection is closed");
      }
   }
}
