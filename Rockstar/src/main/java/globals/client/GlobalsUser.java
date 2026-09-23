package globals.client;


public final class GlobalsUser {
   private final String username;
   private final String password;
   private final String client;

   public GlobalsUser(String localValue1, String localValue2, String localValue3) {
      this.username = localValue1;
      this.password = localValue2;
      this.client = localValue3;
   }

   @Override
   public final String toString() {
      return "GlobalsUser[username=" + this.username() + ", password=" + this.password() + ", client=" + this.client() + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.username());
      result = 31 * result + java.util.Objects.hashCode(this.password());
      result = 31 * result + java.util.Objects.hashCode(this.client());
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      GlobalsUser other = (GlobalsUser) localValue1;
      return java.util.Objects.equals(this.username(), other.username())
         && java.util.Objects.equals(this.password(), other.password())
         && java.util.Objects.equals(this.client(), other.client());
   }

   public String username() {
      return this.username;
   }

   public String password() {
      return this.password;
   }

   public String client() {
      return this.client;
   }
}
