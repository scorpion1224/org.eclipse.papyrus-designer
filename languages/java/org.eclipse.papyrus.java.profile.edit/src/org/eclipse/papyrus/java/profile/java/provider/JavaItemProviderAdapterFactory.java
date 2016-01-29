/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.papyrus.java.profile.java.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.papyrus.java.profile.java.util.JavaAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class JavaItemProviderAdapterFactory extends JavaAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public JavaItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaClass} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaClassItemProvider javaClassItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaClass}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaClassAdapter() {
		if (javaClassItemProvider == null) {
			javaClassItemProvider = new JavaClassItemProvider(this);
		}

		return javaClassItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaPackage_} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaPackage_ItemProvider javaPackage_ItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaPackage_}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaPackage_Adapter() {
		if (javaPackage_ItemProvider == null) {
			javaPackage_ItemProvider = new JavaPackage_ItemProvider(this);
		}

		return javaPackage_ItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaMethod} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaMethodItemProvider javaMethodItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaMethod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaMethodAdapter() {
		if (javaMethodItemProvider == null) {
			javaMethodItemProvider = new JavaMethodItemProvider(this);
		}

		return javaMethodItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaParameter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaParameterItemProvider javaParameterItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaParameterAdapter() {
		if (javaParameterItemProvider == null) {
			javaParameterItemProvider = new JavaParameterItemProvider(this);
		}

		return javaParameterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaPropertyItemProvider javaPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaPropertyAdapter() {
		if (javaPropertyItemProvider == null) {
			javaPropertyItemProvider = new JavaPropertyItemProvider(this);
		}

		return javaPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.PrimitiveType} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected PrimitiveTypeItemProvider primitiveTypeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.PrimitiveType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createPrimitiveTypeAdapter() {
		if (primitiveTypeItemProvider == null) {
			primitiveTypeItemProvider = new PrimitiveTypeItemProvider(this);
		}

		return primitiveTypeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaProjectItemProvider javaProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaProjectAdapter() {
		if (javaProjectItemProvider == null) {
			javaProjectItemProvider = new JavaProjectItemProvider(this);
		}

		return javaProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.papyrus.java.profile.java.JavaSrcFolder} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected JavaSrcFolderItemProvider javaSrcFolderItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.papyrus.java.profile.java.JavaSrcFolder}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createJavaSrcFolderAdapter() {
		if (javaSrcFolderItemProvider == null) {
			javaSrcFolderItemProvider = new JavaSrcFolderItemProvider(this);
		}

		return javaSrcFolderItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>) type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void dispose() {
		if (javaClassItemProvider != null) {
			javaClassItemProvider.dispose();
		}
		if (javaPackage_ItemProvider != null) {
			javaPackage_ItemProvider.dispose();
		}
		if (javaMethodItemProvider != null) {
			javaMethodItemProvider.dispose();
		}
		if (javaParameterItemProvider != null) {
			javaParameterItemProvider.dispose();
		}
		if (javaPropertyItemProvider != null) {
			javaPropertyItemProvider.dispose();
		}
		if (primitiveTypeItemProvider != null) {
			primitiveTypeItemProvider.dispose();
		}
		if (javaProjectItemProvider != null) {
			javaProjectItemProvider.dispose();
		}
		if (javaSrcFolderItemProvider != null) {
			javaSrcFolderItemProvider.dispose();
		}
	}

}
