<?php

/**
 *
 * abcVote (https://github.com/EVGStudents/abcVote)
 * Bulletin-Board for abcVote's e-voting applications
 *
 * CertificateEntity.php: an object representing a certificate
 *
 * @author Sebastian Nellen <sebastian at nellen.it>
 */

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
      // no id if we're creating as it comes from the database
      if(isset($data['email'])) {
        $this->email = $data['email'];
      }
      $this->certificate = $data['certificate'];
    }

    /**
    * method returning this certificate's associated email address
    */
    public function getEmailAdress() {
        return $this->email;
    }
    /**
    * method returning this certificate's content
    */
    public function getCertificate() {
        return $this->certificate;
    }
}
