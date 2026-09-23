package aethereal.core;

import aethereal.api.InternalApi;
import aethereal.lib.log4j.Logger;
import aethereal.lib.log4j.LoggerContextFactory;
import aethereal.lib.log4j.Provider;
import aethereal.lib.log4j.StatusLogger;
import aethereal.util.ProviderUtil;
import java.net.URL;
import java.security.Permission;
import java.util.Collection;
import org.osgi.annotation.bundle.Header;
import org.osgi.annotation.bundle.Headers;
import org.osgi.framework.AdaptPermission;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

@Headers({@Header(
      name = "Bundle-Activator",
      value = "${@class}"
   ), @Header(
      name = "Bundle-ActivationPolicy",
      value = "lazy"
   )})
@InternalApi
public class Activator implements BundleActivator, SynchronousBundleListener {
   private static final SecurityManager a = System.getSecurityManager();
   private static final Logger b = StatusLogger.x();
   private boolean c;

   private static void a(Permission permission) {
      if (a != null) {
         a.checkPermission(permission);
      }
   }

   private void a(Bundle bundle) {
      if (bundle.getState() != 1) {
         try {
            a(new AdminPermission(bundle, "resource"));
            a(new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
            BundleContext bundleContext = bundle.getBundleContext();
            if (bundleContext == null) {
               b.a("Bundle {} has no context (state={}), skipping loading provider", bundle.getSymbolicName(), this.a(bundle.getState()));
            } else {
               this.a(bundleContext, (BundleWiring)bundle.adapt(BundleWiring.class));
            }
         } catch (SecurityException var3) {
            b.a("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), var3);
         } catch (Exception var4) {
            b.f("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), var4);
         }
      }
   }

   private String a(int state) {
      switch (state) {
         case 1:
            return "UNINSTALLED";
         case 2:
            return "INSTALLED";
         case 4:
            return "RESOLVED";
         case 8:
            return "STARTING";
         case 16:
            return "STOPPING";
         case 32:
            return "ACTIVE";
         default:
            return Integer.toString(state);
      }
   }

   private void a(BundleContext bundleContext, BundleWiring bundleWiring) {
      try {
         Collection<ServiceReference<Provider>> serviceReferences = bundleContext.getServiceReferences(Provider.class, "(APIVersion>=2.6.0)");
         Provider maxProvider = null;

         for (ServiceReference<Provider> serviceReference : serviceReferences) {
            Provider provider = (Provider)bundleContext.getService(serviceReference);
            if (maxProvider == null || provider.d() > maxProvider.d()) {
               maxProvider = provider;
            }
         }

         if (maxProvider != null) {
            ProviderUtil.a(maxProvider);
         }
      } catch (InvalidSyntaxException var8) {
         b.b("Invalid service filter: (APIVersion>=2.6.0)", var8);
      }

      for (URL url : bundleWiring.findEntries("META-INF", "log4j-provider.properties", 0)) {
         ProviderUtil.a(url, bundleWiring.getClassLoader());
      }
   }

   public void start(BundleContext bundleContext) throws Exception {
      ProviderUtil.c.lock();
      this.c = true;
      BundleWiring self = (BundleWiring)bundleContext.getBundle().adapt(BundleWiring.class);

      for (BundleWire wire : self.getRequiredWires(LoggerContextFactory.class.getName())) {
         this.a(bundleContext, wire.getProviderWiring());
      }

      bundleContext.addBundleListener(this);
      Bundle[] bundles = bundleContext.getBundles();

      for (Bundle bundle : bundles) {
         this.a(bundle);
      }

      this.a();
   }

   private void a() {
      if (this.c && !ProviderUtil.b.isEmpty()) {
         ProviderUtil.c.unlock();
         this.c = false;
      }
   }

   public void stop(BundleContext bundleContext) throws Exception {
      bundleContext.removeBundleListener(this);
      this.a();
   }

   public void a(BundleEvent event) {
      this.bundleChanged(event);
   }

   public void bundleChanged(BundleEvent event) {
      switch (event.getType()) {
         case 2:
            this.a(event.getBundle());
            this.a();
      }
   }
}
