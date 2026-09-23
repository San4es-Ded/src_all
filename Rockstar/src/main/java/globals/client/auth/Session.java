package globals.client.auth;


public final class Session {
   private final String access;
   private final String refresh;
   private final long expiresAt;
   private final String username;
   private final Integer uid;
   private final String role;

   public Session(String localValue1, String localValue2, long localValue3, String localValue5, Integer localValue6, String localValue7) {
      this.access = localValue1;
      this.refresh = localValue2;
      this.expiresAt = localValue3;
      this.username = localValue5;
      this.uid = localValue6;
      this.role = localValue7;
   }

   public boolean stale() {
      return System.currentTimeMillis() > this.expiresAt - 60000L;
   }

   @Override
   public final String toString() {
      return "Session[access=" + this.access() + ", refresh=" + this.refresh() + ", expiresAt=" + this.expiresAt() + ", username=" + this.username() + ", uid=" + this.uid() + ", role=" + this.role() + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.access());
      result = 31 * result + java.util.Objects.hashCode(this.refresh());
      result = 31 * result + java.util.Objects.hashCode(this.expiresAt());
      result = 31 * result + java.util.Objects.hashCode(this.username());
      result = 31 * result + java.util.Objects.hashCode(this.uid());
      result = 31 * result + java.util.Objects.hashCode(this.role());
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      Session other = (Session) localValue1;
      return java.util.Objects.equals(this.access(), other.access())
         && java.util.Objects.equals(this.refresh(), other.refresh())
         && java.util.Objects.equals(this.expiresAt(), other.expiresAt())
         && java.util.Objects.equals(this.username(), other.username())
         && java.util.Objects.equals(this.uid(), other.uid())
         && java.util.Objects.equals(this.role(), other.role());
   }

   public String access() {
      return this.access;
   }

   public String refresh() {
      return this.refresh;
   }

   public long expiresAt() {
      return this.expiresAt;
   }

   public String username() {
      return this.username;
   }

   public Integer uid() {
      return this.uid;
   }

   public String role() {
      return this.role;
   }
}
