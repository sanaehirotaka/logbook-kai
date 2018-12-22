function FindProxyForURL(url, host) {

    if ( isInNet(host, "203.104.209.71","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.87","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.184.215","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.183","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.150","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.134","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.167","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.199","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.7","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.39","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.71","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.103","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.135","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.167","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.215","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "125.6.189.247","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.23","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.39","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.55","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

    if ( isInNet(host, "203.104.209.102","255.255.255.255") ) {
      return "PROXY 127.0.0.1:{port}";
    }

  return "DIRECT";
}
