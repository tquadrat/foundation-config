/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 *  <p>{@summary This module provides a facility for the runtime configuration
 *  of a program.} The core component is a class, the {@code Configuration},
 *  that holds the configuration values. Basically, this class is a POJO (or,
 *  more precisely, a JavaBean) that will be generated during compilation from
 *  an annotated interface by an Annotation Processor (refer to the project
 *  {@code org.tquadrat.foundation.config.ap}).</p>
 *  <p>The configuration values themselves may have various origins that are
 *  defined by the respective annotations; it is also possible that a value can
 *  can have multiple origins.</p>
 *  <p>In particular, these origins are possible:</p>
 *  <ul>
 *      <li>The values can be provided during the generation.</li>
 *      <li>The values can be set programmatically, either through the setters
 *      defined in the interface, or by an implementation of
 *      {@link java.util.Map#put(java.lang.Object, java.lang.Object) Map.put()}
 *      that will be generated.</li>
 *      <li>Other possible sources are the
 *      {@linkplain java.lang.System#getProperties() System Properties}
 *      and the
 *      {@linkplain java.lang.System#getenv() Environment settings}.</li>
 *      <li>Configuration values can be set from the command line, both as
 *      Options and Arguments.</li>
 *      <li>The values can be read from the
 *      {@link java.util.prefs.Preferences Preferences}, and, in case of User
 *      Preferences, they can also stored there.</li>
 *      <li>The values can be read from a
 *      {@linkplain java.util.Properties Java Properties}
 *      file.</li>
 *  </ul>
 *  <p>Other origins, like Windows INI files (read and write), XML, JSON and
 *  JavaScript are planned.</p>
 *  <p>Unless otherwise stated, {@code null} argument values will cause
 *  methods and constructors of all classes in this package to throw an
 *  {@link java.lang.Exception Exception},
 *  usually a
 *  {@link org.tquadrat.foundation.exception.NullArgumentException},
 *  but in some rare cases, it could be also a
 *  {@link java.lang.NullPointerException}.</p>
 *
 *  <hr>
 *  <p>User Manual</p>
 *  <h2>Table of Contents</h2>
 *  <ol>
 *      <li>{@href #h2_introduction Introduction}</li>
 *      <li>{@href #h2_usage Usage}</li>
 *      <li>
 *      <ol>
 *          <li>{@href #h3_basicconfiguration Basic Configuration}</li>
 *          <li>
 *          <ol>
 *              <li>{@href #h4_initialisation Initialisation}</li>
 *              <li>{@href #h4_limitations Limitations}</li>
 *              <li>{@href #h4_annotations The Annotations for the basic Configuration Bean}</li>
 *              <li>{@href #h4_defaultgetters <code>default</code> Getters and Setters}</li>
 *          </ol>
 *          </li>
 *          <li>{@href #h3_cmdlineparser Parsing and Interpretation of the Command Line}</li>
 *          <li>{@href #h3_preferences Storage and Retrieval of Preferences}</li>
 *      </ol>
 *      </li>
 *  </ol>
 *  <h2>{@anchor #h2_introduction Introduction}</h2>
 *  <p>As already said above, the core functionality is the generation of a
 *  <i>configuration bean</i> through an annotation processor, based on the
 *  <i>configuration bean specification</i>, an annotated interface that itself
 *  extends the interface
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec}.
 *  On how to configure the build tool of choice to use the annotation
 *  processor, refer to the Javadoc for the
 *  {@code org.tquadrat.foundation.config.ap} project; this project implements
 *  the annotation processor.</p>
 *  <p>The specification interface defines getter and setter methods as
 *  described in chapter&nbsp;7 of the JavaBeans&trade; Specification, with the
 *  extension that getters for {@code boolean} may be prefixed with
 *  &quot;{@code is}&quot; instead of &quot;{@code get}&quot;. Implicitly,
 *  those getters and setters will define also the properties of the
 *  configuration bean.</p>
 *  <p>Aside that the generation of a JavaBean from just an interface will save
 *  some manual work, a specification that extends only
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec}
 *  is boring. Additional functionality can be added to the configuration bean
 *  by extending additional interfaces as described in the following
 *  chapters.</p>
 *  <p>There are two different types of configuration beans: one
 *  &quot;global&quot; one and a session specific version, called
 *  &quot;<i>SessionConfigurationBean</i>&quot;, that uses an interface
 *  extending
 *  {@link org.tquadrat.foundation.config.SessionBeanSpec}
 *  as specification for the generator. The latter allows multiple instances
 *  that are distinguished by a &quot;<i>session key</i>&quot;. Such a session
 *  key can be any arbitrary unique value.</p>
 *
 *  <h2>{@anchor #h2_usage Usage}</h2>
 *  <p>As already mentioned, a configuration bean will be generated from the
 *  configuration bean specification during compile time; although the
 *  generated source code might be accessible &mdash; depending on the
 *  development environment in use &mdash; it is strongly discouraged to modify
 *  the generated code.</p>
 *  <p>To obtain an instance of the generated configuration bean, call
 *  {@link org.tquadrat.foundation.config.ConfigUtil#getConfiguration(java.lang.Class, org.tquadrat.foundation.function.tce.TCEFunction) org.tquadrat.foundation.config.ConfigUtil.getConfiguration()}
 *  like this:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>final AConfigBean configBean = ConfigUtil.getConfiguration( AConfigBean.class, c -&gt; c.getConstructor().newInstance() );
 *  <span class="source-line-no">003</span>&hellip;</pre></div>
 *  <p>For a session bean, the call looks like this:</p>
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>String sessionKey = &hellip;
 *  <span class="source-line-no">003</span>&hellip;
 *
 *  <span class="source-line-no">013</span>&hellip;
 *  <span class="source-line-no">014</span>final ASessionBean sessionBean = ConfigUtil.getConfiguration( ASessionBean.class, sessionKey, (c, s) -&gt; c.getConstructor( String.class ).newInstance( s ) );
 *  <span class="source-line-no">015</span>&hellip;</pre></div>
 *
 *  <p>The lambda expression that is provided as the last parameter is required
 *  to circumvent some limitations that are introduced by the module system:
 *  usually, the package that contains the generated configuration bean is not
 *  accessible for the code in the {@code ConfigUtil} class. As a consequence,
 *  that code cannot instantiate the generated class.</p>
 *  <p>But the lambda expression is code that can access the generated code,
 *  and the lambda can be executed by the code in {@code ConfigUtil}, so this
 *  construct allows to create an instance of the generated configuration
 *  bean.</p>
 *  <p>When using the API provided by {@code ConfigUtil}, each call to
 *  {@code getConfiguration()} with the same arguments will return the same
 *  instance of the configuration bean.</p>
 *  <p>As having a call like that above in production code looks a bit clumsy,
 *  it is recommended to add a {@code static} factory method to the
 *  configuration bean specification interface, like this:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>public static AConfigBean getInstance()
 *  <span class="source-line-no">003</span>{
 *  <span class="source-line-no">004</span>    return ConfigUtil.getConfiguration( AConfigBean.class, c -&gt; c.getConstructor().newInstance() );
 *  <span class="source-line-no">005</span>}
 *  <span class="source-line-no">006</span>&hellip;</pre></div>
 *
 *  <p>This would allow to obtain a reference to the configuration bean like
 *  this:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>final var configBean = AConfigBean.getInstance();
 *  <span class="source-line-no">003</span>&hellip;</pre></div>
 *
 *  <div style="border-style: solid; border-radius: 8px; margin-left: 10px; padding-left:5px; padding-right:5px;">
 *  <p>Annotation processing does not support the manipulation of source code
 *  before it will be compiled, at least not without breaking some rules: the
 *  class {@code JCTree}, that does provide some basic capabilities for that
 *  purpose, belongs to the package {@code com.sun.tools.javac.tree<}, and that
 *  is not exported by the module <code>jdk.compiler</code>, at least not to
 *  the public.</p>
 *  <p>Perhaps a future version of annotation processing will provide that
 *  capability, or we try an implementation with byte code manipulation.</p>
 *  </div>
 *
 *  <h3>{@anchor #h3_basicconfiguration Basic Configuration}</h3>
 *  <p>A simple configuration bean specification interface may look like
 *  this:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>package com.sample.test;
 *  <span class="source-line-no">002</span>
 *  <span class="source-line-no">003</span>import java.util.Optional;
 *  <span class="source-line-no">004</span>
 *  <span class="source-line-no">005</span>import org.tquadrat.foundation.ui.configuration.CheckEmpty;
 *  <span class="source-line-no">006</span>import org.tquadrat.foundation.ui.configuration.ConfigBeanBase;
 *  <span class="source-line-no">007</span>import org.tquadrat.foundation.ui.configuration.ConfigurationBeanSpecification;
 *  <span class="source-line-no">008</span>
 *  <span class="source-line-no">009</span>&#64;ConfigurationBeanSpecification
 *  <span class="source-line-no">010</span>public interface ASimpleConfigurationBean extends ConfigBeanBase
 *  <span class="source-line-no">011</span>{
 *  <span class="source-line-no">012</span>    public Optional&lt;String&gt; getConfigValue();
 *  <span class="source-line-no">013</span>
 *  <span class="source-line-no">014</span>    public boolean isFlag();
 *  <span class="source-line-no">015</span>
 *  <span class="source-line-no">016</span>    &#64;CheckEmpty
 *  <span class="source-line-no">017</span>    public void setConfigValue( final String value );
 *  <span class="source-line-no">018</span>}</pre></div>
 *
 *  <p>This would generate a configuration bean that looks similar to
 *  <a href="org/tquadrat/foundation/config/tools/doc-files/ASimpleConfigurationBeanImpl.java.html">this</a>.</p>
 *  <p>The final bean contains several methods more than those specified in the
 *  specification interface: these are defined in the interface
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec ConfigBeanSpec}.
 *  Aside some technical configuration values like the time zone and the
 *  locale, these are
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#addListener(org.tquadrat.foundation.config.ConfigurationChangeListener) addListener()}
 *  that allows to add a listener to the configuration bean to observe any
 *  changes to the configuration, and
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#removeListener(org.tquadrat.foundation.config.ConfigurationChangeListener) removeListener()}
 *  that removes that listener in the end.</p>
 *  <p>Internally, the configuration bean uses an instance of
 *  {@link org.tquadrat.foundation.config.spi.ConfigChangeListenerSupport}
 *  to manage the listeners, and to distribute the change events.</p>
 *  <p>The listener references are implemented as
 *  {@linkplain java.lang.ref.WeakReference weak references}
 *  to make memory dissipation a bit more unlikely.</p>
 *  <p>When firing events, the method
 *  {@link org.tquadrat.foundation.config.ConfigurationChangeListener#propertyChange(org.tquadrat.foundation.config.ConfigurationChangeEvent) propertyChange()}
 *  is called on each listener in a new thread.</p>
 *
 *  <h4>{@anchor #h4_initialisation Initialisation}</h4>
 *  <p>The configuration bean properties can be initialised from the
 *  {@href #h3_cmdlineparser command line},
 *  from
 *  {@href #h3_preferences preferences},
 *  or from a resource that is configured through the
 *  {@href #h5_configurationbeanspecification <code>&#64;ConfigurationBeanSpecification</code>}
 *  annotation.</p>
 *  <p>The resource has to be a standard properties file as it will be written
 *  by
 *  {@link java.util.Properties#store(java.io.OutputStream, java.lang.String) Properties.store()},
 *  with the name of the configuration bean property as the name for file. The
 *  values have to be String representations that can be translated to the type
 *  of the property by the respective
 *  {@link org.tquadrat.foundation.lang.StringConverter StringConverter}.</p>
 *  <p>An additional (or alternative) approach would be to provide a default or
 *  static method with the signature</p>
 *
 *  <div class="source-container"><pre>Map&lt;String,Object&gt; initData() throws Exception</pre></div>
 *
 *  <p>with the configuration bean specification. This method returns the
 *  initialisation data for the configuration bean properties in a
 *  {@code Map}.</p>
 *  <p>The property name is the key for the {@code Map}, and the associated
 *  value is the value for the property, <i>as the proper type</i>, not as its
 *  String representation! Neither the key nor the value may be {@code null},
 *  but it is not necessary to provide values for all properties defined by the
 *  configuration bean specification.</p>
 *  <p>The method {@code initData()} will be called from the constructor of the
 *  configuration bean; if it throws an Exception, the constructor will
 *  terminate with an
 *  {@link java.lang.ExceptionInInitializerError}.</p>
 *  <p>If both a resource and the method {@code initData()} are provided, the
 *  data from the resource is applied first, than the data from the method.</p>
 *  <p>Finally, the properties that are annotated with either
 *  {@code &#64;SystemProperty} or {@code &#64;EnvironmentVariable} will be
 *  initialised from the system properties ({@code System.getProperty()}) or the
 *  environment variables ({@code System.getenv()}) accordingly.</p>
 *  <p>The methods
 *  {@link org.tquadrat.foundation.config.CLIBeanSpec#parseCommandLine(java.lang.String[]) CLIBeanSpec.parseCommandLine()}
 *  and
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec#loadPreferences() PreferencesBeanSpec.loadPreferences()}
 *  will not be called automatically, they have to be called from user code.
 *  They may overwrite previously set values.</p>
 *
 *  <h4>{@anchor #h4_limitations Limitations}</h4>
 *  <p>Some types are more or less inappropriate for a configuration bean
 *  attribute, basically those that are mutable so that different calls to the
 *  getter for that property may return different results. For some types, this
 *  is manageable, like for all the collection classes
 *  ({@link java.util.List},
 *  {@link java.util.Set},
 *  {@link java.util.Map}),
 *  for
 *  {@link java.lang.StringBuilder},
 *  for
 *  {@link java.lang.StringBuffer}
 *  or for
 *  {@link java.util.Calendar};
 *  for others, it is difficult. Therefore an
 *  {@link java.lang.Error}
 *  will be thrown for the types</p>
 *  <ul>
 *      <li>{@link java.io.InputStream} and anything that is derived from this
 *      class</li>
 *      <li>{@link java.util.stream.Stream}</li>
 *      <li>{@link java.util.stream.DoubleStream}</li>
 *      <li>{@link java.util.stream.IntStream}</li>
 *      <li>{@link java.util.stream.LongStream}</li>
 *  </ul>
 *  <p>The getter for a property with a type implementing
 *  {@link java.util.List},
 *  {@link java.util.Set},
 *  or
 *  {@link java.util.Map}
 *  will always return an immutable copy of the {@code List}, {@code Set}, or
 *  {@code Map}, created through a call to
 *  {@link java.util.List#copyOf(java.util.Collection) List.copyOf()},
 *  {@link java.util.Set#copyOf(java.util.Collection) Set.copyOf()},
 *  or
 *  {@link java.util.Map#copyOf(java.util.Map) Map.copyOf()},
 *  respectively. This means that {@code null} values are not allowed, for a
 *  {@code Map} neither as key nor as value.</p>
 *  <p>The {@href #h4_initialisation initialisation} works only for types that
 *  have a known implementation of
 *  {@link org.tquadrat.foundation.lang.StringConverter StringConverter},
 *  for
 *  {@link java.util.Date},
 *  and for all {@code enum} types. {@code Date} is special, as an instance is
 *  to be represented as the milliseconds since the begin of the epoch
 *  (1970-01-01T00:00:00 UTC).
 *
 *  <h4>{@anchor #h4_annotations The Annotations for the basic Configuration Bean}</h4>
 *  <p>As seen in the sample, the behaviour of a configuration property can be
 *  changed by adding some annotations to the getter and setter methods.</p>
 *  <p>For a simple configuration bean without additional features, this are
 *  the annotations</p>
 *  <ul>
 *      <li><a href="#h5_checkempty"><code>&#64;CheckEmpty</code></a></li>
 *      <li><a href="#h5_checknull"><code>&#64;CheckNull</code></a></li>
 *      <li><a href="#h5_propertyname"><code>&#64;PropertyName</code></a></li>
 *      <li><a href="#h5_specialproperty"><code>&#64;SpecialProperty</code></a></li>
 *      <li><a href="#h5_systemproperty"><code>&#64;SystemProperty and &#64;EnvironmentVariable</code></a></li>
 *  </ul>
 *  <p>The annotation
 *  {@href #h5_configurationbeanspecification <code>&#64;ConfigurationBeanSpecification</code>}
 *  is special as it is not applied to a method, but to the interface itself;
 *  in fact this is what marks an interface as a configuration bean
 *  specification.</p>
 *
 *  <h5><a id="h5_checkempty"></a>{@link org.tquadrat.foundation.config.CheckEmpty &#64;CheckEmpty}</h5>
 *  <p>The annotation <code>&#64;CheckEmpty</code> is allowed only for setters,
 *  and there only for those that take an argument of type
 *  {@link java.lang.String},
 *  {@link java.lang.CharSequence}
 *  or
 *  {@link java.util.Optional},
 *  or a collection type like
 *  {@link java.util.List}
 *  or
 *  {@link java.util.Map}.</p>
 *  <p>It triggers the code generator to add a check on empty or {@code null}
 *  to the setter method, like in code below:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>&#47;**
 *  <span class="source-line-no">003</span> * {&#64;inheritDoc}
 *  <span class="source-line-no">004</span> *&#47;
 *  <span class="source-line-no">005</span>&#64;Override
 *  <span class="source-line-no">006</span>public final void setConfigValue( final String _$value )
 *  <span class="source-line-no">007</span>{
 *  <span class="source-line-no">008</span>    try( final var l = m_WriteLock.lock() ) {
 *  <span class="source-line-no">009</span>        var oldValue = m_ConfigValue;
 *  <span class="source-line-no">010</span>        m_ConfigValue = <b>requireNotEmptyArgument( _$value, "_$value" )</b>;
 *  <span class="source-line-no">011</span>        m_ListenerSupport.fireEvent( "configValue", oldValue, m_ConfigValue );
 *  <span class="source-line-no">012</span>    }
 *  <span class="source-line-no">013</span>}  &#47;&#47;  setConfigValue()
 *  <span class="source-line-no">014</span>&hellip;</pre></div>
 *
 *  <h5><a id="h5_checknull"></a>{@link org.tquadrat.foundation.config.CheckEmpty &#64;CheckNull}</h5>
 *  <p>Similar to the annotation <code>&#64;CheckEmpty</code> described
 *  {@href #h5_checkempty above},
 *  the annotation <code>&#64;CheckNull</code> adds an argument validation to a
 *  setter that takes any non-primitive argument; in this case, it checks that
 *  the given argument value is not {@code null}:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>&#47;**
 *  <span class="source-line-no">003</span> * {&#64;inheritDoc}
 *  <span class="source-line-no">004</span> *&#47;
 *  <span class="source-line-no">005</span>&#64;Override
 *  <span class="source-line-no">006</span>public final void setConfigValue( final Date _$value )
 *  <span class="source-line-no">007</span>{
 *  <span class="source-line-no">008</span>    try( final var l = m_WriteLock.lock() ) {
 *  <span class="source-line-no">009</span>        var oldValue = m_ConfigValue;
 *  <span class="source-line-no">010</span>        m_ConfigValue = <b>requireNonNullArgument( _$value, "_$value" )</b>;
 *  <span class="source-line-no">011</span>        m_ListenerSupport.fireEvent( "configValue", oldValue, m_ConfigValue );
 *  <span class="source-line-no">012</span>    }
 *  <span class="source-line-no">013</span>}  &#47;&#47;  setConfigValue()
 *  <span class="source-line-no">014</span>&hellip;</pre></div>
 *
 *  <p>Obviously,the two annotations {@code &#64;CheckNull} and
 *  {@code &#64;CheckEmpty} are mutually exclusive.</p>
 *
 *  <h5><a id="h5_configurationbeanspecification"></a>{@link org.tquadrat.foundation.config.ConfigurationBeanSpecification &#64;ConfigurationBeanSpecification}</h5>
 *  <p>Primarily, this annotation marks an interface that extends
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec}
 *  as a configuration bean specification. But it also allows to determine the
 *  name of the generated class for the configuration bean.</p>
 *  <p>The default behaviour is that for a specification interface named
 *  {@code com.sample.application.MyConfig} the class name for the bean will be
 *  {@code com.sample.application.generated.MyConfigImpl}. But if
 *  {@code MyConfig} is annotated with</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>&#64;ConfigurationBeanSpecification( name = "MyConfigBean", samePackage = true )
 *  <span class="source-line-no">003</span>public interface MyConfig
 *  <span class="source-line-no">004</span>{
 *  <span class="source-line-no">005</span>    &hellip;</pre></div>
 *
 *  <p>the class name would be {@code com.sample.application.MyConfigBean}.</p>
 *  <p>It should be obvious that the fully qualified name of the generated
 *  configuration bean class needs to be distinct from any other class
 *  name.</p>
 *  <p>If the configuration bean specification interface is an inner class,the
 *  enclosing class(es) will be ignored when determining the name of the class
 *  for the generated configuration bean.</p>
 *  <p>The attribute
 *  {@link org.tquadrat.foundation.config.ConfigurationBeanSpecification#initDataResource() initDataResource}
 *  defines a resource that is used to initialise the configuration bean, as
 *  described {@href #h4_initialisation above}.</p>
 *
 *  <h5><a id="h5_propertyname"></a>{@link org.tquadrat.foundation.annotation.PropertyName &#64;PropertyName}</h5>
 *  <p>The annotation {@code &#64;PropertyName} is allowed for getters, setters
 *  and &quot;{@code add}&quot; methods; it provides a mechanism to modify the
 *  name of the property. Usually the property name is determined from the name
 *  of the method:</p>
 *  <ul>
 *      <li>{@code getValue()} results in the property name {@code value}</li>
 *      <li>{@code isFlag()} results in the property name {@code flag}</li>
 *      <li>{@code setSwitch} results in the property name {@code switch}</li>
 *  </ul>
 *  <p>With the argument to the annotation {@code &#64;PropertyName},that
 *  property name can be changed to any other arbitrary value &ndash; given
 *  that it is a valid Java identifier.</p>
 *  <p>The code</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>&#64;PropertyName( "StartDate" )
 *  <span class="source-line-no">003</span>public Date getConfigValue();
 *  <span class="source-line-no">004</span>&hellip;</pre></div>
 *
 *  <p>in the configuration bean specification interface means that there will
 *  be a property {@code StartDate} in the generated configuration bean and no
 *  property {@code configValue}.</p>
 *  <p>This feature is especially useful with &quot;{@code add}&quot; methods
 *  (that are described {@href #h4_addmethods below}): while the property for
 *  the collection itself is usually named with the plural form
 *  ({@code getEvents()}), the &quot;{@code add}&quot; method uses the singular
 *  form: {@code addEvent()}.To connect the latter to the property
 *  {@code events} instead of {@code event},the annotation
 *  {@code &#64;PropertyName} will be applied to the method.</p>
 *
 *  <h5><a id="h5_specialproperty"></a>{@link org.tquadrat.foundation.config.SpecialProperty &#64;SpecialProperty}</h5>
 *  <p><i>Special Properties</i>are configuration properties that have a
 *  somehow special behaviour; they will be initialised in a special way or do
 *  have a special meaning or alike. The types of the available special
 *  properties are defined in
 *  {@link org.tquadrat.foundation.config.SpecialPropertyType}.</p>
 *  <p>Getters and sometimes setters for some of the special properties are
 *  already defined in
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec}
 *  and
 *  {@link org.tquadrat.foundation.config.SessionBeanSpec},
 *  others can be added to the configuration bean specification. Not all
 *  special properties do allow both getters and setters; refer to the
 *  respective description for the type of the special property.</p>
 *
 *  <h5><a id="h5_systemproperty"></a>{@link org.tquadrat.foundation.config.SystemProperty &#64;SystemProperty}
 *  and
 *  {@link org.tquadrat.foundation.config.EnvironmentVariable &#64;EnvironmentVariable}</h5>
 *  <p>These annotations are allowed only for getters, and if there is no
 *  default {@code StringConverter} for the type of the property, an
 *  implementation for a String converter has to be provided with the
 *  annotation.</p>
 *  <p>The value for the annotation is the name of a
 *  {@linkplain java.lang.System#getProperty(java.lang.String) system property}
 *  or a
 *  {@linkplain java.lang.System#getenv(java.lang.String) environment variable}
 *  that is used to initialise the annotated configuration property.</p>
 *  <p>System properties can be set programmatically with a call to
 *  {@link java.lang.System#setProperty(java.lang.String,java.lang.String) System.setProperty()}
 *  or with the {@code -D} option on the JVM's arguments list, while
 *  environment variables cannot be manipulated from inside the running
 *  program.</p>
 *  <p>Calling the setter for the configuration property will not modify
 *  neither the system property nor the environment variable,and a change of
 *  the system property that occurs after the configuration bean was
 *  initialised is not reflected to the configuration property.</p>
 *  <p>The {@code &#64;EnvironmentVariable} is especially handy when writing
 *  code that should run later in a Docker (or similar) container environment
 *  as these are quite often configured through environment variables.</p>
 *
 *  <h4>{@anchor #h4_defaultgetters <code>default</code> Getters and Setters}</h4>
 *  <p>Since Java&nbsp;8, it is possible to provide implementations in
 *  interfaces, by using the {@code default} modifier for the methods. Of
 *  course, this is also possible for a configuration bean specification.
 *  Usually, these methods are ignored by the annotation processor, only
 *  getters and setters that are marked as {@code default} need some special
 *  attention.</p>
 *  <ul>
 *      <li>None of the annotations defined in this module may be applied to
 *      one of the {@code default} getters or setters</li>
 *      <li>A {@code default} setter is not allowed without a {@code default}
 *      getter</li>
 *      <li>The property that is handled by that {@code default} method is not
 *      backed by a preference value</li>
 *  </ul>
 *
 *  <h4>{@anchor #h4_addmethods &quot;<code>Add</code>&quot;Methods}</h4>
 *  <p>For properties of the types
 *  {@link java.util.List List&lt;T&gt;},
 *  {@link java.util.Set Set&lt;T&gt;},
 *  and
 *  {@link java.util.Map Map&lt;K,V&gt;}
 *  it is possible to have methods that add new values to the collection.Such
 *  a method would be named <code>add&lt;<i>PropertyName</i>&gt;</code> and
 *  takes a single argument of the collection's element type. The methods will
 *  be generated always to throw a
 *  {@link org.tquadrat.foundation.exception.NullArgumentException}
 *  when the given argument is {@code null}, like if the method would be
 *  annotated with {@code &#64;checkEmpty}.</p>
 *
 *  <h3>{@anchor #h3_cmdlineparser Parsing and Interpretation of the Command Line}</h3>
 *  <p>This feature is based on an original idea from <i>Kohsuke Kawaguchi</i>
 *  ({@href mailto:kk@kohsuke.org kk@kohsuke.org}) and <i>Mark Sinke</i>, but
 *  their implementation evaluated the annotation that defined the options and
 *  arguments during runtime. Since Java&nbsp;9and the introduction of Jigsaw,
 *  this approach does not work that well anymore: the annotations had to be
 *  placed either to the ({@code private}) fields or the setters for these
 *  fields *&ndash;that might be {@code private}, too, when the value should be
 *  read-only.</p>
 *  <p>So if the old approach should work any longer,the annotated class needs
 *  to be in an exported module; that may be feasible, but as a requirement,
 *  this is too much of a limitation.</p>
 *  <p>Therefore the whole stuff had been re-implemented on base of an
 *  annotation processor, evaluating the annotations at compile time.</p>
 *  <p>In addition, the API itself can be used also in a programmatic form and
 *  with an XML configuration file; this is described in detail on the
 *  overview page for the package
 *  {@link org.tquadrat.foundation.config.cli}.</p>
 *  <p>To use the Command Line (CLI) feature with the annotation processor, a
 *  configuration property just needs to be annotated with one of the
 *  annotations
 *  {@href #h4_cliannotations below}.
 *  Parsing the command line is then as easy as:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">001</span>&hellip;
 *  <span class="source-line-no">002</span>public final static int main( final String...args )
 *  <span class="source-line-no">003</span>{
 *  <span class="source-line-no">004</span>    &hellip;
 *  &hellip;
 *  <span class="source-line-no">014</span>    &hellip;
 *  <span class="source-line-no">015</span>    final var configBean = AConfigBean.getInstance();
 *  <span class="source-line-no">016</span>    configBean.setResources( resourceBundle );
 *  <span class="source-line-no">017</span>    if( !configBean.parseCommandLine( args ) )
 *  <span class="source-line-no">018</span>    {
 *  <span class="source-line-no">019</span>        configBean.retrieveParseErrorMessage.ifPresent( out::println );
 *  <span class="source-line-no">020</span>        configBean.printUsage( out, &quot;command&quot; );
 *  <span class="source-line-no">021</span>    }
 *  <span class="source-line-no">022</span>    &hellip;
 *  &hellip;
 *  <span class="source-line-no">032</span>    &hellip;
 *  <span class="source-line-no">033</span>}  // main()
 *  <span class="source-line-no">034</span>&hellip;</pre></div>
 *
 *  <p>That's all!</p>
 *  <p>Confessed, there is a little bit more: not all data types can be
 *  annotated, only those with an implementation of
 *  {@link org.tquadrat.foundation.config.cli.CmdLineValueHandler}
 *  existing on the{@code CLASSPATH}at compile time and during execution.The
 *  library knows already several value handlers, others can be provided by the
 *  program or other libraries,too.</p>
 *
 *  <h4>{@anchor #h4_cliannotations The CLI Annotations}</h4>
 *  <p>For the Command Line(CLI)feature,two annotations were added to the UI
 *  library:</p>
 *  <ul>
 *      <li>{@link org.tquadrat.foundation.config.Argument &#64;Argument}</li>
 *      <li>{@link org.tquadrat.foundation.config.Option &#64;Option}</li>
 *  </ul>
 *  <p>They have to be placed on the getter method for a property, and either
 *  an {@code &#64;Argument} or an {@code &#64;Option} are allowed, but not
 *  both.</p>
 *  <p>The two annotations do have mainly the same attributes,described
 *  below:</p>
 *  <dl>
 *      <dt>{@code String[] aliases}</dt>
 *      <dd><p>This attribute is only known by the {@code &#64;Option}
 *      annotation; it provides additional option names (see below) that follow
 *      the same rules. The default value is the empty array.</p></dd>
 *
 *      <dt>{@code String format}</dt>
*       <dd><p>Some value handlers (like the
*       {@link org.tquadrat.foundation.config.cli.DateValueHandler DateValueHandler}
 *      or those value handler implementations that are derived from
 *      {@link org.tquadrat.foundation.config.cli.TimeValueHandler TimeValueHandler})
 *      use this field for additional validation information, like a format
 *      String or a regular expression. It is ignored by most others. Refer to
 *      the documentation of those value handlers for the exact contents
 *      specification.</p>
 *      <p>The default is the empty String.</p></dd>
 *
 *      <dt>{@code Class<?extends CmdLineValueHandler> handler}</dt>
 *      <dd><p>Specifies the
 *      {@linkplain org.tquadrat.foundation.config.cli.CmdLineValueHandler command line value handler}
 *      that translates the command line argument value to the type of the
 *      target property and places that value to the property.</p>
 *      <p>If not set, the effective
 *      {@link org.tquadrat.foundation.config.cli.CmdLineValueHandler CmdLineValueHandler}
 *      implementation will be inferred from the type of the annotated
 *      property, given that such a handler is registered;otherwise, an
 *      exception is thrown.</p>
 *      <p>If this annotation attribute is set, it overrides the inference and
 *      determines the handler to be used. This does not only allow the support
 *      for previously unknown data-types, it is also convenient for defining a
 *      non-standard option parsing semantics:</p>
 *      <div class="source-container"><pre>      <span class="source-line-no">001</span>&hellip;
 *      <span class="source-line-no">002</span>// this is a normal argument, allowing true and false
 *      <span class="source-line-no">003</span>&#064;Argument( index = 0 )
 *      <span class="source-line-no">004</span>boolean getFlag();
 *      <span class="source-line-no">005</span>
 *      <span class="source-line-no">006</span>// This causes that MyHandler is used instead of the default handler provided
 *      <span class="source-line-no">007</span>// for boolean; now Yes and No can be used instead of true and false
 *      <span class="source-line-no">008</span>&#064;Argument( index = 1, handler = MyHandler.class )
 *      <span class="source-line-no">009</span>boolean getYesNo();
 *      <span class="source-line-no">010</span>&hellip;</pre></div></dd>
 *
 *      <dt>{@code int index}</dt>
 *      <dd><p>This attribute is only known by the {@code &#64;Argument}
 *      annotation.</p>
 *      <p>A command line argument is identified by its relative position (its
 *      'index') on the command line, instead by a name. The first position has
 *      the index value 0, the second is 1 and so on. No gaps are allowed in
 *      the numbering of the arguments.</p></dd>
 *
 *      <dt>{@code String metaVar}</dt>
 *      <dd><p>A descriptive name for the argument or for the option parameter
 *      that is used in messages. If left unspecified,that name is inferred
 *      from the name of the configuration property itself for an argument, or
 *      from the type of the property for an option.</p>
 *      <p>For the property &quot;{@code position}&quot; that is provided as an
 *      argument and the property &quot;{@code location}&quot; that is provided
 *      as the value for the option &quot;{@code --loc}&quot;, this may look
 *      like this:</p>
 *
 *      <blockquote><pre><code>Usage: command --loc <b>STRING</b> <b>POSITION</b></code></pre></blockquote></dd>
 *
 *      <dt>{@code boolean multiValued}</dt>
 *      <dd><p>A flag that indicates whether the argument or option is
 *      multi-valued, for mappings to a
 *      {@link java.util.Collection Collection}.</p>
 *      <p>As this will consume all remaining arguments from the command line,
 *      the so annotated property has to be the last argument.</p>
 *      <p>For an option, it means that the same option is allowed to appear
 *      multiple times on the command line.</p></dd>
 *
 *      <dt>{@code String name}</dt>
 *      <dd><p>As {@code aliases} above, this attribute is only valid for the
 *      {@code &#64;Option} annotation; it provides the name for the command
 *      line option for the annotated property.</p>
 *      <p>Such an option name (as well as an option alias) has to be either a
 *      single dash (&quot;-&quot;), followed by a single character (a
 *      <i>short option</i>), or two dashes followed by more than one character
 *      (<i>long option</i>).</p>
 *      <p>The name may contain letters, numbers, and most special characters
 *      that are allowed on a command line (excluding the equals sign
 *      &quot;=&quot; &ndash; for obvious reasons &ndash;, double and single
 *      quotes), but no whitespace characters.</p>
 *      <p>Some examples are:</p>
 *      <ul>
 *          <li>{@code -r}</li>
 *          <li>{@code --port}</li>
 *          <li>{@code -1} &ndash; valid but not really recommended</li>
 *          <li>{@code --} &ndash; invalid, but allowed on the command line,
 *          having a special meaning there</li>
 *          <li>{@code -} &ndash; invalid</li>
 *          <li>{@code -name} &ndash; invalid: has to begin with <i>two</i>
 *          dashes</li>
 *          <li>{@code --f} &ndash; invalid: not enough characters after the
 *          two dashes; would be valid as '{@code -f}'</li>
 *          <li>{@code --port-number} &ndash; valid,but dashes within the name
 *           are discouraged</li>
 *           <li>{@code --port number} &ndash; invalid because of the blank
 *           between <i>port</i> and <i>number</i></li>
 *           <li>{@code --port_number}</li>
 *           <li>{@code -@} &ndash; valid but strongly discouraged (see
 *           {@href #h4_cmdline below})</li>
 *           <li>{@code -?} &ndash; valid; usually used to request a help
 *           output</li>
 *      </ul>
 *      </dd>
 *
 *      <dt>{@code boolean required}</dt>
 *      <dd><p>A flag that specifies whether this argument or option is
 *      mandatory.</p>
 *      <p>For an argument, this implies that all previous arguments (those
 *      with lower indexes) are mandatory as well.</p></dd>
 *
 *      <dt>{@code String usage}</dt>
 *      <dd><p>A help text that will be displayed in the usage output (see
 *      {@link org.tquadrat.foundation.config.CLIBeanSpec#printUsage(java.io.OutputStream,java.lang.CharSequence) CLIBeanSpec.printUsage()})
 *      when
 *      {@link org.tquadrat.foundation.config.ConfigBeanSpec#getResourceBundle() ConfigBeanSpec.getResourceBundle()}
 *      does not return a
 *      {@link java.util.ResourceBundle ResourceBundle}
 *      instance or the call to
 *      {@link java.util.ResourceBundle#getString(String) getString()}
 *      with the value of &quot;{@code usageKey}&quot; (see below) on the
 *      retrieved resources throws a
 *      {@link java.util.MissingResourceException}.</p>
 *      <p>Will be empty if not provided.</p></dd>
 *
 *      <dt>{@code String usageKey}</dt>
 *      <dd><p>The
 *      {@linkplain java.util.ResourceBundle#getString(String) resource bundle key}
 *      for a help text that will be displayed in the usage output (see
 *      {@link org.tquadrat.foundation.config.CLIBeanSpec#printUsage(java.io.OutputStream,java.lang.CharSequence) CLIBeanSpec.printUsage()}).</p>
 *      <p>If not specified, the value will be derived from the name of the
 *      property like this:</p>
 *
 *      <blockquote><pre><code>USAGE_KEY-&lt;<i>PropertyName</i>&gt;</code></pre></blockquote>
 *
 *      <p>The text will be retrieved from the
 *      {@link java.util.ResourceBundle ResourceBundle}
 *      that is returned from
 *      {@link org.tquadrat.foundation.config.ConfigBeanSpec#getResourceBundle() ConfigBeanSpec.getResourceBundle()};
 *      if that is {@code null} the value of &quot;{@code usage()}&quot; (see
 *      above) is taken instead.</p>
 *      <p>This allows to localise the usage output.</p></dd>
 *  </dl>
 *
 *  <h4>{@anchor #h4_cmdline The Command Line}</h4>
 *  <p>The command line parser recognises command lines according to this
 *  pattern:</p>
 *
 *  <blockquote><pre><code>[{<b>&#64;</b>&lt;<i>filename</i>&gt;|<b>-</b>&lt;<i>o</i>&gt;[[ ][&lt;<i>parameter</i>&gt;]]|<b>--</b>&lt;<i>option</i>&gt;[{ |<b>=</b>}&lt;<i>parameter</i>&gt;]}&hellip;][ <b>--</b>][ {<b>&#64;</b>&lt;<i>filename</i>&gt;|&lt;<i>argument</i>&gt;]}&hellip;]</code></pre></blockquote>
 *
 *  <p>This means that</p>
 *  <ul>
 *      <li><code><b>-o</b><i>parameter</i></code></li>
 *      <li><code><b>-o</b> <i>parameter</i></code></li>
 *      <li><code><b>--option</b> <i>parameter</i></code></li>
 *      <li><code><b>--option</b>=<i>parameter</i></code></li>
 *  </ul>
 *  <p>are all valid.</p>
 *  <p><code><b>&#64;</b>&lt;<i>filename</i>&gt;</code> stands for an argument
 *  file containing a single command line entry per line. This means that an
 *  argument file with the contents</p>
 *
 *  <blockquote><pre><code>  # This is an argument file
 *  --flag
 *  --option1
 *  value
 *  --option2=other value
 *  --option3
 *  yet another value
 *  argument
 *  another argument</code></pre></blockquote>
 *
 *  <p>is parsed like the command line</p>
 *
 *  <blockquote><pre><code>--flag --option1 value --option2 "other value" --option3 "yet another value" argument "another argument"</code></pre></blockquote>
 *
 *  <p>The line beginning with the hash (&quot;#&quot;) is treated as a
 *  comment.</p>
 *  <p>The contents of an argument is copied to the command line at the
 *  location of the reference to that file: if {@code argfile} has the
 *  contents</p>
 *
 *  <blockquote><pre><code>  --flag
 *  --option
 *  value
 *  argument</code></pre></blockquote>
 *
 *  <p>and the command line was given like</p>
 *
 *  <blockquote><pre><code>  --loc NONE &#64;argfile</code></pre></blockquote>
 *
 *  <p>the final sequence is</p>
 *
 *  <blockquote><pre><code>--loc NONE --flag --option value argument</code></pre></blockquote>
 *
 *  <p>The token &quot;{@code --}&quot; means that after it no more options
 *  will follow. So after {@code --},an argument like
 *  &quot;{@code --dashes}&quot; might be valid.</p>
 *
 *  <h3>{@anchor #h3_preferences Storage and Retrieval of Preferences}</h3>
 *  <p>With the package
 *  {@link java.util.prefs}
 *  an API exists that can be used to persist program settings between
 *  different runs of an application. The different implementations of that API
 *  utilise mechanisms that are specific to the operating system where the
 *  program will be executed. So it will use the &quot;Registry&quot; if
 *  running on Microsoft Windows or special files in the {@code home} folder on
 *  a Linux system.</p>
 *  <p>This library connects the configuration values (at least most of them)
 *  with preferences values, using the name of the configuration bean
 *  specification as the name of the node and the key is the name of the
 *  property. Both can be modified by annotations.</p>
 *  <p>To enable the preferences support for a configuration bean
 *  specification, it needs to extend the interface
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec}.</p>
 *  <p>That interface provides the methods
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec#loadPreferences()}
 *  that reads the preference values, and
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec#updatePreferences()}
 *  to write the values back.</p>
 *  <p>Per default, all custom properties that have a supported type will be
 *  backed by preferences. If a preference is unwanted for a certain property,
 *  it can be annotated with
 *  {@link org.tquadrat.foundation.config.NoPreference &#64;NoPreference}.</p>
 *  <p>&quot;Supported types&quot; are all those types for that an
 *  implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  exists; some will be detected automatically, for others the class of the
 *  implementation can be provided with the
 *  {@link org.tquadrat.foundation.config.Preference &#64;Preference}
 *  annotation.</p>
 *
 *  <h4>{@code USER} vs. {@code SYSTEM}</h4>
 *  <p>The Preferences API distinguishes between system-wide preferences and
 *  user-specific preferences. Per default, the configuration bean properties
 *  are treated as user properties, because system-wide properties may need
 *  special authorisation to read and especially to write them. As a
 *  consequence it is not possible to write back system-wide properties with
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec#updatePreferences() PreferencesBeanSpec.updatePreferences()}.</p>
 *
 *  <div style="border-style: solid; border-radius: 8px; margin-left: 10px; padding-left:5px; padding-right:5px;">
 *      <p>On a UNIX based system (this includes Linux and MacOS), the
 *      preferences are stored in the file system.</p>
 *      <p>For MacOS, the preferences files generated by the Preferences API
 *      are named {@code com.apple.java.util.prefs.plist}. The user's
 *      preferences file is stored in their home directory
 *      ({@code ~/Library/Preferences/}). The system preferences are stored in
 *      {@code /Library/Preferences/}and are only persisted to disk if the user
 *      is an administrator. Some more details can be found
 *      {@href https://stackoverflow.com/questions/675864/where-are-java-preferences-stored-on-mac-os-x on StackOverflow}.</p>
 *      <p>On UNIX and Linux, the location for the user's preferences is set
 *      with the system property
 *      &quot;{@value org.tquadrat.foundation.lang.CommonConstants#PROPERTY_PREFS_ROOT_USER}&quot;;
 *      if not set, it is {@code ~}, the user's home directory. This would then
 *      result in the path
 *      <code>&lt;<i>path</i>&gt;/.java/.userPrefs/&lt;<i>nodeName</i>&gt;/prefs.xml</code>.</p>
 *      <p>The location for the system preferences is set with the system
 *      property
 *      &quot;{@value org.tquadrat.foundation.lang.CommonConstants#PROPERTY_PREFS_ROOT_SYSTEM}&quot;;
 *      if not set, it is either {@code /etc} or {@code $JAVA_HOME}, depending
 *      on the JVM.This would then result in the path
 *      <code>&lt;<i>path</i>&gt;/.java/.systemPrefs/&lt;<i>nodeName</i>&gt;/prefs.xml</code>.</p>
 *      <p>In case a node has child nodes, there is a folder hierarchy
 *      underneath the &lt;<i>nodeName</i>&gt; folder. Some more details can be
 *      found again
 *      {@href https://stackoverflow.com/questions/1320709/preference-api-storage on StackOverflow}.</p>
 *      <p>In case the node name contains characters that are not appropriate
 *      for a UNIX folder name &mdash; according to the logic of the API, this
 *      includes the dot('.',0x2e) and the underscore ('_',0x5f) &mdash; the
 *      folder name will be the BASE64 encrypted form of the node name,
 *      prepended with an underscore ('_',0x5f)(sic!). As the the default name
 *      for the preferences node for a configuration bean is its fully
 *      qualified class name &mdash; containing dots &mdash; the folder name
 *      for that preferences file is usually something cryptic like
 *      &quot;{@code _!':!bw"t!#4!cw"h!'0!c!"s!'`!.g"0!'`!cw"0!#4!~w"l!'4!~@"y!'%!d!"l!'@!.g"$!'8!bg"m!'k!~w"1!()!}@"0!'k!bw"u!%)!~@"h!'4!]@"t!(!!b!==}&quot;
 *      (this is for &quot;{@code com.sample.test.generated.ConfigurationBeanImpl}&quot;).</p>
 *  </div>
 *
 *  <h3>Implementation of
 *  {@link java.util.Map}</h3>
 *  <p>A configuration bean specification interface can extend the interface
 *  {@link java.util.Map java.util.Map&lt;String,Object&gt;};
 *  the required methods will be generated automatically.</p>
 *  <p>The {@code Map} interface provides reading access to the property
 *  values, the mutating methods of {@code Map} (like
 *  {@link java.util.Map#clear() clear() }
 *  or
 *  {@link java.util.Map#put(Object,Object) put()})
 *  will throw an
 *  {@link java.lang.UnsupportedOperationException}.</p>
 *  <p>If the {@code Map} feature is used, the configuration bean specification
 *  may not define a getter method {@code isEmpty()} as this would collide with
 *  the
 *  {@linkplain java.util.Map#isEmpty() method with the same name}
 *  from the interface {@code Map}.</p>
 *
 *  <h3>i18n and Configuration Beans</h3>
 *  <p>A configuration bean is the perfect source for messages and texts. By
 *  adding the interface
 *  {@link org.tquadrat.foundation.config.I18nSupport}
 *  to the configuration bean specification, the configuration bean will be
 *  enabled as such a source for texts and messages.</p>
 *  <p>It uses the
 *  {@link java.util.Locale Locale}
 *  that will be returned by
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#getLocale() ConfigBeanSpec.getLocale()}
 *  to identify the resource bundle that will be returned by
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#getResourceBundle() ConfigBeanSpec.getResourceBundle()};
 *  a call to
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#setLocale(java.util.Locale) ConfigBeanSpec.setLocale()}
 *  will change the language of the texts and messages (if there are
 *  translations for the new language, of course).</p>
 *  <p>When the i18n support is used, it is not recommended to define a setter
 *  for the {@code resourceBundle} property.</p>
 */

package org.tquadrat.foundation.config;

/*
 *  End of File
 */