import HlsTypes from 'hls.js';
const Hls: typeof HlsTypes = require('hls.js');

class MyHls {
  private _videoSrc: string;
  private _isSupported: boolean;
  private _hls: HlsTypes;

  /**
   * https://github.com/video-dev/hls.js/blob/master/docs/API.md#getting-started
   * @param adNumber
   */
  constructor(adNumber: string) {
    this._videoSrc = window.location.origin + `/video/${adNumber}/master.m3u8`;
    this._hls = new Hls({ enableWorker: false, debug: true });
    this._isSupported = Hls.isSupported();
  }

  run(media: HTMLMediaElement) {
    if (media.canPlayType('application/vnd.apple.mpegurl')) {
      console.log("It's natively supported");
      media.src = this._videoSrc;
      media.play();
    } else if (this._isSupported) {
      this.attachMedia(media);

      this._hls.on(Hls.Events.MEDIA_ATTACHED, () => {
        console.log('video and hls.js are now bound together !');

        this._hls.loadSource(this._videoSrc);

        this._hls.on(Hls.Events.MANIFEST_PARSED, (event, data) => {
          console.log('manifest loaded, found ' + data.levels.length + ' quality level');

          media.play();
        });
      });

      this.handleError();
    }
  }

  attachMedia(media: HTMLMediaElement) {
    this._hls.attachMedia(media);
  }

  private handleError() {
    this._hls.on(Hls.Events.ERROR, (event, data) => {
      console.log('---data: ', data);
      switch (data.type) {
        case Hls.ErrorTypes.NETWORK_ERROR:
          // try to recover network error
          console.warn('fatal network error encountered, try to recover');
          this._hls.startLoad();
          break;
        case Hls.ErrorTypes.MEDIA_ERROR:
          console.warn('fatal media error encountered, try to recover');
          this._hls.recoverMediaError();
          break;
        default:
          // cannot recover
          console.error('Fatal error occured: ', data);
          this._hls.destroy();
          break;
      }
    });
  }
}

export default MyHls;
