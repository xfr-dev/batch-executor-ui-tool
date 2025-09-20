import groovy.beans.Bindable
import groovy.transform.ToString

@ToString(includeNames=true)
class Options {
    @Bindable
    Boolean autoCloseOutputWindow = false
}