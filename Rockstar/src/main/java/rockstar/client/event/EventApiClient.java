package rockstar.client.event;



import rockstar.client.data.*;
import rockstar.client.*;
import rockstar.client.internal.network.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EventApiClient {
   private final String internalField0248;
   private final String internalField0247;
   private final Gson internalField0931;

   public EventApiClient(String localValue1) {
      this("https://events.felon.su/api", localValue1);
   }

   public EventApiClient(String localValue1, String localValue2) {
      this.internalField0248 = localValue1.endsWith("/") ? localValue1.substring(0, localValue1.length() - 1) : localValue1;
      this.internalField0247 = localValue2;
      this.internalField0931 = new Gson();
   }

   public List<ServerEventInfo> internalMethod04948() throws IOException, InterruptedException {
      String localValue1 = this.internalMethod06757("/events");
      Type localValue2 = (new TypeToken<List<ServerEventInfo>>() {}).getType();
      return (List<ServerEventInfo>)this.internalField0931.fromJson(localValue1, localValue2);
   }

   public List<FunTimeMineInfo> internalMethod02513() throws IOException, InterruptedException {
      String localValue1 = this.internalMethod06757("/mines");
      Type localValue2 = (new TypeToken<List<FunTimeMineInfo>>() {}).getType();
      return (List<FunTimeMineInfo>)this.internalField0931.fromJson(localValue1, localValue2);
   }

   private String internalMethod06757(String localValue1) throws IOException {
      HttpDownloadUtils.InternalType0449 localValue2 = HttpDownloadUtils.internalMethod02390(
         this.internalField0248 + localValue1, "X-System-Token", this.internalField0247, "Accept", "application/json"
      );
      if (localValue2.internalMethod02739() == 401) {
         throw new SecurityException("Authentication failed: Invalid system API token.");
      } else if (localValue2.internalMethod02739() == 403) {
         throw new SecurityException("Access denied: Forbidden resource.");
      } else if (!localValue2.internalMethod02740()) {
         throw new IOException("Server error: HTTP " + localValue2.internalMethod02739() + " - " + localValue2.internalMethod00694());
      } else {
         return localValue2.internalMethod00694();
      }
   }
}
