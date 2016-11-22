<?php
class CertificateEntity
{
    protected $email;
    protected $certificate;
    /**
     * Accept an array of data matching properties of this class
     * and create the class
     *
     * @param array $data The data to use to create
     */
    public function __construct(array $data) {
      // no id if we're creating
      if(isset($data['email'])) {
        $this->email = $data['email'];
      }
      $this->certificate = $data['certificate'];
    }
    public function getEmailAdress() {
        return $this->email;
    }
    public function getCertificate() {
        return $this->certificate;
    }
}
