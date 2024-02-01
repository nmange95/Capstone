import ChatBox from './ChatBox';
import Navbar from './Navbar';
import SupportButton from './SupportButton';
import AboutFooterText from './AboutFooterText';

const Layout = ({ children }) => {
    return (
        <>
            <div className="layout">
                <Navbar />
                <main>{children}</main>
                <ChatBox />
                <SupportButton />
                <AboutFooterText />
            </div>
        </>
    );
};

export default Layout;
